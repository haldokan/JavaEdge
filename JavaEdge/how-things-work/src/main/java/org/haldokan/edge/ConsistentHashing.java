package org.haldokan.edge;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * My implementation of Consistent Hashing with support form removing and adding hash buckets (we use a cluster
 * of machines to represent buckets).
 * todo: add machine aliases to mitigate the case where 2 machines are too close to each other in the hash ring
 *
 * The Question: 5_STAR
 *
 * Created by haytham.aldokanji on 8/16/16.
 */
public class ConsistentHashing {
    private final NavigableSet<Double> hashRing;
    private final double HASH_SLOT_LEN = 1d / Integer.MAX_VALUE;
    HashFunction hashFunction = Hashing.md5();
    // we could have used 2 maps but Guava's BiMap is a more concise way to do it
    private BiMap<Double, Machine> machineByPosition;
    private Map<String, Machine> machineById = new HashMap<>();

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
        for (String machineId : machineIds) {
            double position = Math.random();
            hashRing.add(position);
            System.out.printf("hashRing: %s%n", hashRing);

            machineByPosition.put(position, machineById.get(machineId));
            if (hashRing.size() > 1) {
                rehashDataRange(position);
            }
        }
    }

    public void removeMachines(String... machineIds) {
        // we are shutting down the cluster - in real app we should verify that machine ids actually exist in the cluster
        if (machineIds.length >= hashRing.size()) {
            hashRing.clear();
            machineByPosition.clear();
        } else {
            // here Guava BiMap comes to play
            for (String machineId : machineIds) {
                Machine machine = machineById.get(machineId);
                double machinePosition = machineByPosition.inverse().get(machine);
                hashRing.remove(machinePosition);
                machineByPosition.inverse().remove(machine);
                //todo find next machine and unload data to it: similar to rehashDataRange (can we refactor)?
            }
        }
    }

    public void processDataItem(DataItem dataItem) {
        Machine machine = findMachine(dataItem.getKey());
        machine.processDataItem(dataItem);
    }

    private void rehashDataRange(Double newMachinePosition) {
        Machine newMachine = machineByPosition.get(newMachinePosition);
        Machine potentialNextMachine = machineByPosition.get(hashRing.lower(newMachinePosition));
        Machine nextMachine = potentialNextMachine != null ? potentialNextMachine : machineByPosition.get(hashRing.last());


        Set<DataItem> dataItemsForRedistribution = nextMachine.getDataItemsForRedistribution(dataItem -> {
            double dataItemHash = Math.abs(hashFunction.hashString(dataItem,
                    Charset.defaultCharset()).asInt() * HASH_SLOT_LEN);
            return dataItemHash > newMachinePosition;
        });

        dataItemsForRedistribution.forEach(newMachine::processDataItem);
    }

    private Machine findMachine(String data) {
        int hashCode = hashFunction.hashString(data, Charset.defaultCharset()).asInt();
        double hashPosition = Math.abs(hashCode * HASH_SLOT_LEN);
        // counter clockwise find the nearest machine
        System.out.printf("%f%n", hashPosition);

        // if position is less than the position of the 1st machine then no machine will be found
        Double potentialPosition = hashRing.floor(hashPosition);
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
            if (i == 8000) {
                Machine extraMachine3 = new Machine("MID" + i);
                machineById.put(extraMachine3.getId(), extraMachine3);
                addMachines(extraMachine3.getId());
            }
        }
    }

    private static final class Machine {
        private final String id;

        private Map<String, DataItem> data = new ConcurrentHashMap<>();

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
            Set<String> keys = data.keySet().stream().filter(hashPredicate::test).collect(Collectors.toSet());
            Set<DataItem> dataItems = new HashSet<>();

            for (String key : keys) {
                dataItems.add(data.remove(key));
            }
            return dataItems;
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

        public byte[] getPayload() {
            return payload;
        }

        @Override
        public String toString() {
            return "DataItem{" +
                    "key='" + key + '\'' +
                    '}';
        }
    }
}
