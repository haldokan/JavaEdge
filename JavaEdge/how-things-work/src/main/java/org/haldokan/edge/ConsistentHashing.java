package org.haldokan.edge;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * My implementation of Consistent Hashing with support for removing and adding hash buckets (I use a cluster
 * of machines arranged in a ring for the concrete implementation).
 *
 * todo: add machine aliases to mitigate the case where 2 machines are too close to each other in the hash ring
 *
 * The Question: 5_STAR
 *
 * From Wikipedia: Consistent hashing is a special kind of hashing such that when a hash table is resized, only
 * K/n keys need to be remapped on average, where K is the number of keys, and n is the number of slots. In contrast,
 * in most traditional hash tables, a change in the number of array slots causes nearly all keys to be remapped because
 * the mapping between the keys and the slots is defined by a modular operation.
 *
 * Created by haytham.aldokanji on 8/16/16.
 */
public class ConsistentHashing {
    private final NavigableSet<Double> hashRing;
    private final double HASH_SLOT_LEN = 1d / Integer.MAX_VALUE;
    private final HashFunction hashFunction;
    // we could have used 2 maps but Guava's BiMap is a more concise way to do it
    private final BiMap<Double, Machine> machineByPosition;
    private final Map<String, Machine> machineById = new HashMap<>();

    public ConsistentHashing() {
        this(Hashing.md5());
    }

    public ConsistentHashing(HashFunction hashFunction) {
        hashRing = new ConcurrentSkipListSet<>(); // sorted and thread-safe
        machineByPosition = HashBiMap.create(new ConcurrentHashMap<>());
        this.hashFunction = hashFunction;
    }

    public static void main(String[] args) {
        ConsistentHashing driver = new ConsistentHashing();
        driver.test();
    }

    public void addMachines(String... machineIds) {
        System.out.printf("Add machines: %s%n", Arrays.toString(machineIds));

        for (String machineId : machineIds) {
            double position = Math.random();
            hashRing.add(position);

            Machine machine = machineById.get(machineId);
            machineByPosition.put(position, machine);

            if (hashRing.size() > 1) {
                redistributeDataOnAddingMachine(machine, position);
            }
        }
    }

    public void removeMachines(String... machineIds) {
        System.out.printf("Remove machines: %s%n", Arrays.toString(machineIds));

        // we are shutting down the cluster - in real app we should verify that machine ids actually exist in the cluster
        if (machineIds.length >= hashRing.size()) {
            hashRing.clear();
            machineByPosition.clear();
        } else {
            // here Guava BiMap comes to play
            List<Machine> removedMachines = new ArrayList<>();
            List<Double> removedPositions = new ArrayList<>();

            // remove all machines before redistributing the data
            for (String machineId : machineIds) {
                Machine machine = machineById.get(machineId);
                double machinePosition = machineByPosition.inverse().get(machine);
                hashRing.remove(machinePosition);
                machineByPosition.inverse().remove(machine);

                removedMachines.add(machine);
                removedPositions.add(machinePosition);
            }

            Iterator<Machine> machineIterator = removedMachines.iterator();
            for (double removedPosition : removedPositions) {
                redistributeDataOnRemovingMachine(machineIterator.next(), removedPosition);
            }
        }
    }

    public void processDataItem(DataItem dataItem) {
        int hashCode = hashFunction.hashString(dataItem.getKey(), Charset.defaultCharset()).asInt();
        double hashPosition = Math.abs(hashCode * HASH_SLOT_LEN);

        Machine machine = findMachine(hashPosition, hashRing::floor);
        machine.processDataItem(dataItem);
    }

    private void redistributeDataOnAddingMachine(Machine machine, Double machinePosition) {
        Machine nextMachine = findMachine(machinePosition, hashRing::lower);

        Set<DataItem> dataItemsForRedistribution = nextMachine.getDataItemsForRedistribution(dataItem -> {
            double dataItemHash = Math.abs(hashFunction.hashString(dataItem,
                    Charset.defaultCharset()).asInt() * HASH_SLOT_LEN);
            return dataItemHash > machinePosition;
        });

        dataItemsForRedistribution.forEach(machine::processDataItem);
    }

    private void redistributeDataOnRemovingMachine(Machine machine, Double machinePosition) {
        Set<DataItem> dataItemsForRedistribution = machine.getDataItemsForRedistribution(dataItem -> true);
        Machine nextMachine = findMachine(machinePosition, hashRing::lower);
        dataItemsForRedistribution.forEach(nextMachine::processDataItem);
    }

    private Machine findMachine(double hashPosition, Function<Double, Double> positionFunc) {
        Double potentialPosition = positionFunc.apply(hashPosition);
        // circle from start to end of the set taking the machine at the highest position
        double machinePosition = potentialPosition != null ? potentialPosition : hashRing.last();
        return machineByPosition.get(machinePosition);
    }

    private void test() {
        int numOfMachines = 7;
        String[] machineIds = new String[numOfMachines];

        for (int i = 0; i < numOfMachines; i++) {
            String machineId = "MID" + i;
            machineById.put(machineId, new Machine(machineId));
            machineIds[i] = machineId;
        }

        addMachines(machineIds);

        Random random = new Random();
        for (int i = 0; i < 10_000; i++) {
            processDataItem(new DataItem("key" + Math.abs(random.nextInt()),
                    new byte[random.nextInt(1000) + 1]));
            if (i == 3000) {
                Machine extraMachine1 = new Machine("MID" + i);
                machineById.put(extraMachine1.getId(), extraMachine1);

                Machine extraMachine2 = new Machine("MID" + (i + 1));
                machineById.put(extraMachine2.getId(), extraMachine2);

                addMachines(extraMachine1.getId(), extraMachine2.getId());
            }

            if (i == 6000) {
                removeMachines("MID3000");
            }

            if (i == 8000) {
                Machine extraMachine3 = new Machine("MID" + i);
                machineById.put(extraMachine3.getId(), extraMachine3);
                addMachines(extraMachine3.getId());
            }

            if (i == 9500) {
                removeMachines("MID8000");
            }
        }
    }

    private static final class Machine {
        private final String id;

        private final Map<String, DataItem> data = new ConcurrentHashMap<>();

        public Machine(String id) {
            this.id = id;
        }

        public void processDataItem(DataItem dataItem) {
            data.put(dataItem.getKey(), dataItem);
            System.out.printf("%s: %d%n", id, data.size());
        }

        public String getId() {
            return id;
        }

        public Set<DataItem> getDataItemsForRedistribution(Predicate<String> hashPredicate) {
            Set<String> keys = data.keySet().stream()
                    .filter(hashPredicate)
                    .collect(Collectors.toSet());

            return keys.stream().map(data::remove).collect(Collectors.toSet());
        }

        @Override
        public String toString() {
            return "Machine{" +
                    "id='" + id + '\'' +
                    '}';
        }
    }

    private static class DataItem {
        private final String key;
        private final byte[] payload;

        public DataItem(String key, byte[] payload) {
            this.key = key;
            this.payload = payload;
        }

        public String getKey() {
            return key;
        }

        @Override
        public String toString() {
            return "DataItem{" +
                    "key='" + key + '\'' +
                    ", payload=" + Arrays.toString(payload) +
                    '}';
        }
    }
}
