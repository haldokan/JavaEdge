package org.haldokan.edge.interviewquest.facebook;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * My solution to a Facebook interview question - solution has a hint of dynamic programming in that we memorize the sets
 * intersections for each pass and update them based on values generated at each pass
 * The Question: 4_STAR
 * <p>
 * The question aims to test both your programming and analytical skills. Your implementation will be tested for both
 * time and space efficiency.
 * <p>
 * The input format is strictly followed. Your program will be evaluated for correctness against multiple inputs.
 * Some inputs will be very large (> 100,000 nodes).
 * <p>
 * <p>
 * Problem Statement
 * <p>
 * You are in-charge of the office jukebox. You are determined to do a very good job and make your colleagues happy.
 * You ask them to email you a list of music bands they like. The number of bands each colleague likes is limited to 10,000.
 * <p>
 * Input
 * <p>
 * All input will be given on stdin. Your input will be of the form,
 * <p>
 * 1. The first line will be an integer stating the number of lines of input.
 * 2. The input will only contain alphanumeric characters, colon, comma, underscore and space [A-Z, a-z, 0-9, _, , ], +.
 * 3. The first word will be the name of your colleague, followed by a colon.
 * 4. A comma separated list of that person’s favourite bands will follow the colon.
 * 5. Every line will be terminated by a newline character (\n).
 * <p>
 * An example input would look like:
 * <p>
 * 6
 * Anne: Metallica, The_Doors, Black_Sabbath
 * John: The_Beatles, The_Doors, Metallica, Pink_Floyd
 * Kathy: U2, Guns_n_Roses, Led_Zeppelin
 * Jamie: Radiohead
 * Ashok: Guns_n_Roses, U2, Pink_Floyd, The_Doors
 * Sara: Blink_182, Iron_Maiden, The_Doors
 * <p>
 * <p>
 * Problem
 * <p>
 * You decide to use your data to find the people most compatible with each other. Two people are compatible if they
 * have at least 2 bands in common. The compatibility of two people is directly proportional to the number of bands they
 * like in common.
 * <p>
 * For each person in the list, output the most compatible person(s). If there is more than one compatible person,
 * separate the names with a comma. If a person has nobody compatible, output nothing. For our example input,
 * the output will be:
 * <p>
 * Anne: John
 * John: Anne, Ashok
 * Kathy: Ashok
 * Jamie:
 * Ashok: John, Kathy
 * Sara:
 * <p>
 * Created by haytham.aldokanji on 7/7/16.
 */
public class FindingCompatibilityBasedOnBandSelection {
    private static final int MIN_COMPATIBILITY_LEVEL = 2;
    private final Map<String, Set<String>> selections = new HashMap<>();

    public static void main(String[] args) {
        testAddSelection();
        testCompatibilities();
    }

    private static void testAddSelection() {
        FindingCompatibilityBasedOnBandSelection driver = new FindingCompatibilityBasedOnBandSelection();
        driver.addSelection("Haytham: U2, Sting, Krall");

        Set<String> bands = driver.selections.get("Haytham");
        assertThat(bands.size(), is(3));
        assertThat(bands, containsInAnyOrder("U2", "Sting", "Krall"));
    }

    private static void testCompatibilities() {
        FindingCompatibilityBasedOnBandSelection driver = new FindingCompatibilityBasedOnBandSelection();
        driver.addSelection("Anne: Metallica, The_Doors, Black_Sabbath");
        driver.addSelection("John: The_Beatles, The_Doors, Metallica, Pink_Floyd");
        driver.addSelection("Kathy: U2, Guns_n_Roses, Led_Zeppelin");
        driver.addSelection("Jamie: Radiohead");
        driver.addSelection("Ashok: Guns_n_Roses, U2, Pink_Floyd, The_Doors");
        driver.addSelection("Sara: Blink_182, Iron_Maiden, The_Doors");

        Multimap<String, String> result = driver.computeCompatibility();
        System.out.printf("%s%n", result);

        assertThat(result.get("John").toArray(), is(new String[]{"Ashok", "Anne"}));
        assertThat(result.get("Ashok").toArray(), is(new String[]{"John", "Kathy"}));
        assertThat(result.get("Kathy").toArray(), is(new String[]{"Ashok"}));
        assertThat(result.get("Anne").toArray(), is(new String[]{"John"}));
        assertThat(result.get("Jamie").isEmpty(), is(true));
        assertThat(result.get("Sara").isEmpty(), is(true));
    }

    public void addSelection(String selection) {
        String[] parts = selection.split(":");
        String name = parts[0].trim();
        String[] bands = parts[1].split(",");

        Arrays.stream(bands).forEach(band -> {
            selections.computeIfAbsent(name, k -> new HashSet<>());
            selections.get(name).add(band.trim());
        });
    }

    public Multimap<String, String> computeCompatibility() {
        String[] names = selections.keySet().toArray(new String[selections.size()]);
        Multimap<String, Compatibility> compatibilityMap = HashMultimap.create();

        for (int i = 0; i < names.length; i++) {
            String name1 = names[i];
            for (int j = i + 1; j < names.length; j++) {
                String name2 = names[j];
                Set<String> bands1 = selections.get(name1);
                Set<String> bands2 = selections.get(name2);
                int numSharedBands = Sets.intersection(bands1, bands2).size();

                if (numSharedBands >= MIN_COMPATIBILITY_LEVEL) {
                    updateCompatibilities(compatibilityMap, name1, name2, numSharedBands);
                    updateCompatibilities(compatibilityMap, name2, name1, numSharedBands);
                }
            }
        }
        Multimap<String, String> result = HashMultimap.create();
        Arrays.stream(names).forEach(name -> {
            Collection<String> compatibilities = new ArrayList<>();
            if (compatibilityMap.containsKey(name)) {
                compatibilities = compatibilityMap.get(name).stream()
                        .map(Compatibility::getName)
                        .collect(Collectors.toList());
            }
            result.putAll(name, compatibilities);
        });
        return result;
    }

    private void updateCompatibilities(Multimap<String, Compatibility> compatibilityMap,
                                       String name1,
                                       String name2,
                                       int numSharedBands) {
        Compatibility compatibility2 = new Compatibility(name2, numSharedBands);

        Collection<Compatibility> compatibilities1 = compatibilityMap.get(name1);
        if (compatibilities1.isEmpty()) {
            compatibilityMap.put(name1, compatibility2);
        } else {
            int numberOfBands = compatibilities1.iterator().next().numberOfBands;
            if (numSharedBands == numberOfBands) {
                compatibilityMap.put(name1, compatibility2);
            } else if (numSharedBands > numberOfBands) {
                compatibilityMap.removeAll(name1);
                compatibilityMap.put(name1, compatibility2);
            }
        }
    }

    private static class Compatibility {
        private final String name;
        private final int numberOfBands;

        public Compatibility(String name, int numberOfBands) {
            this.name = name;
            this.numberOfBands = numberOfBands;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Compatibility that = (Compatibility) o;

            return !(name != null ? !name.equals(that.name) : that.name != null);

        }

        public String getName() {
            return name;
        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }

        @Override
        public String toString() {
            return name + ": " + numberOfBands;
        }
    }
}
