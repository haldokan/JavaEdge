package org.haldokan.edge;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;
import java.util.*;

/**
 * @InProgress consistent hashing with replicas
 * Created by haytham.aldokanji on 8/16/16.
 */
public class ConsistentHashing {
    private final NavigableSet<Double> hashRing;
    private final double HASH_SLOT_LEN = 1d / Integer.MAX_VALUE;
    HashFunction hashFunction = Hashing.md5();
    private Map<Double, Machine> machineByPosition = new HashMap<>();


    public ConsistentHashing() {
        this(Hashing.md5());
    }

    public ConsistentHashing(HashFunction hashFunction) {
        hashRing = new TreeSet<>();
        this.hashFunction = hashFunction;
    }

    public static void main(String[] args) {
        ConsistentHashing driver = new ConsistentHashing();
        driver.test();
    }

    public void addMachines(Machine... machines) {
        for (Machine machine : machines) {
            double position = Math.random();
            hashRing.add(position);
            machineByPosition.put(position, machine);
        }
        System.out.printf("hashRing: %s%n", hashRing);
    }

    public void processData(String data) {
        Machine machine = findMachine(data);
        System.out.printf("Machine: %s%n", machine);
        machine.processData(data);
    }

    private Machine findMachine(String data) {
        HashCode hashCode = hashFunction.hashString(data, Charset.defaultCharset());
        double hashPosition = Math.abs(hashCode.asInt() * HASH_SLOT_LEN);
        // counter clockwise find the nearest machine
        System.out.printf("%f%n", hashPosition);
        double machinePosition = hashRing.floor(hashPosition);
        return machineByPosition.get(machinePosition);
    }

    private void test() {
        int numOfMachines = 7;
        Machine[] machines = new Machine[numOfMachines];

        for (int i = 0; i < numOfMachines; i++) {
            machines[i] = new Machine("MID" + i);
        }

        addMachines(machines);

        for (int i = 0; i < 1000; i++) {
            processData("data" + i);
        }
    }

    private static final class Machine {
        private final String id;
        private List<String> data = new ArrayList<>();

        public Machine(String id) {
            this.id = id;
        }

        public void processData(String dataItem) {
            data.add(dataItem);
        }

        @Override
        public String toString() {
            return "Machine{" +
                    "id='" + id + '\'' +
                    '}';
        }
    }
}
