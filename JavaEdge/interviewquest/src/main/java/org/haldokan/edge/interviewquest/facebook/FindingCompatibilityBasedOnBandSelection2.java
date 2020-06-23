package org.haldokan.edge.interviewquest.facebook;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * My solution to a Facebook interview question
 * I think the solution I implemented here is more to the spirit of this
 * question than FindingCompatibilityBasedOnBandSelection: number of bands can be too large and there will be repetitions.
 * It is better to use the band name as a key in the map and update a graph of weights between different people.
 *
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
 * Created by haytham.aldokanji on 6/23/20.
 */
public class FindingCompatibilityBasedOnBandSelection2 {
    Multimap<String, String> colleaguesByBands = HashMultimap.create();
    Table<String, String, Integer> compatibilityTable = HashBasedTable.create();

    // some lists may be 10,000. They will contain many dups b/w different people
    static String[] data = new String[] {
        "Anne: Metallica, The_Doors, Black_Sabbath",
        "John: The_Beatles, The_Doors, Metallica, Pink_Floyd",
        "Kathy: U2, Guns_n_Roses, Led_Zeppelin",
        "Jamie: Radiohead",
        "Ashok: Guns_n_Roses, U2, Pink_Floyd, The_Doors",
        "Sara: Blink_182, Iron_Maiden, The_Doors"
    };

    public static void main(String[] args) {
        FindingCompatibilityBasedOnBandSelection2 driver = new FindingCompatibilityBasedOnBandSelection2();
        driver.processInput(data);
        System.out.println(driver.getCompatibilityScores());
    }

    void processInput(String[] input) {
        for (String line : input) {
            String[] parts = line.split(":");

            String person = parts[0];
            compatibilityTable.put(person, person, 0);

            String[] bands = Arrays.stream(parts[1].split(",")).map(String::trim).toArray(String[]::new);
            for (String band : bands) {
                colleaguesByBands.put(band, person);
                for (String fan : colleaguesByBands.get(band)) {
                    Integer currentBond = compatibilityTable.get(person, fan);
                    int newBond = currentBond == null ? 1 : currentBond + 1;
                    compatibilityTable.put(person, fan, newBond);
                    compatibilityTable.put(fan, person, newBond);
                }
            }
        }
    }

    Multimap<String, String> getCompatibilityScores() {
        List<Table.Cell<String, String, Integer>> compatibilityList = compatibilityTable.cellSet().stream()
            .filter(c -> !c.getRowKey().equals(c.getColumnKey()))
            .sorted((c1, c2) -> c2.getValue() - c1.getValue())
            .collect(Collectors.toList());
        System.out.println(compatibilityList);
        Multimap<String, String> compatibilityByPerson = ArrayListMultimap.create();
        Map<String, Integer> scoreByPerson = new HashMap<>();

        for (Table.Cell<String, String, Integer> compatibilityItem : compatibilityList) {
            int score = scoreByPerson.computeIfAbsent(compatibilityItem.getRowKey(), k -> compatibilityItem.getValue());
            if (score == compatibilityItem.getValue()) {
                String bond = null;
                if (score > 1) {
                    bond = compatibilityItem.getColumnKey();
                }
                compatibilityByPerson.put(compatibilityItem.getRowKey(), bond);
            }
        }
        return compatibilityByPerson;
    }
}
