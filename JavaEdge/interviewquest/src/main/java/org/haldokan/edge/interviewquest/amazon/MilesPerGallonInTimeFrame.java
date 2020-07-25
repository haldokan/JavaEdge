package org.haldokan.edge.interviewquest.amazon;

import com.google.common.collect.Lists;

import java.time.LocalDateTime;
import java.util.*;

/**
 * My solution to an Amazon interview question - nice data structures question. Knowing of sorted maps helps
 *
 * The Question: 4-STAR
 *
 * A group of friends are tracking the miles per gallon for each of their cars. Each time one of them fills up their gas
 * tank, they record the following in a file:
 *
 * His or her name
 * The type of car they drove
 * How many miles driven since they last filled up
 * How many gallons purchased at this fill-up
 * Date of the fill
 *
 * Their data is formatted as a comma separate value (csv) file with the following format for each
 * row:(#person,carName,milesDriven,gallonsFilled,fillupDate)
 *
 * Miles are recorded as floating-point numbers and gallons as integers.Create a program that allows members of this
 * group to determine the miles per gallon (MPG) of each of their cars during a specific time range.
 * Note: person may have more than one so a time range query might need to output data for one or more cars.
 *
 * A skeleton class will be provided;
 * your job will be to complete the program. The principal function for querying MPG is of the form
 * (the exact name, data types, etc., can be learned by inspecting the "solution" class in the skeleton code):
 *
 * GetRangeMPG(PersonName, StartDate, EndDate) Returns list of objects containing (CarName, MPG) MPG is calculated as
 * (total miles traveled during time period)/ (total gallons filled during time period.
 * The dates you receive in the query should be treated inclusively.
 */
public class MilesPerGallonInTimeFrame {
    // { person -> { car -> { sorted(date) -> gas-consumption } } }
    Map<String, Map<String, SortedMap<LocalDateTime, GasConsumption>>> ledger;

    public static void main(String[] args) {
        List<String> rawLedger = Lists.newArrayList(
          "name1,car1,40.2,4,2020-07-17T21:22:19",
          "name1,car1,48.2,5,2020-07-20T21:22:19",
          "name1,car1,46.2,7,2020-07-21T20:22:19",
          "name1,car2,42.3,3,2020-07-25T21:22:19",
          "name1,car2,42.3,3,2020-07-26T21:22:19",
          "name1,car22,30.3,2,2020-07-27T21:22:19",
          "name2,car3,41.7,5,2020-07-25T21:22:19",
          "name2,car4,40.9,6,2020-07-19T21:22:19",
          "name3,car5,49.3,7,2020-07-25T21:22:19",
          "name4,car6,50.3,4,2020-07-23T21:22:19",
          "name5,car7,40.23,2,2020-07-12T21:22:19",
          "name6,car8,40.1,4,2020-07-22T21:22:19",
          "name6,car9,40.33,5,2020-07-21T21:22:19",
          "name6,car10,39.5,4,2020-07-09T21:22:19"
        );
        MilesPerGallonInTimeFrame driver = new MilesPerGallonInTimeFrame(rawLedger);

        List<CarConsumption> mpgs = driver.milesPerGallon("name1", LocalDateTime.parse("2020-07-20T21:22:19"), LocalDateTime.parse("2020-07-26T21:22:19"));
        System.out.println(mpgs);

        mpgs = driver.milesPerGallon("name1", LocalDateTime.parse("2020-07-16T21:22:19"), LocalDateTime.parse("2020-07-28T21:22:19"));
        System.out.println(mpgs);

        mpgs = driver.milesPerGallon("name6", LocalDateTime.parse("2020-07-09T21:22:19"), LocalDateTime.parse("2020-07-23T21:22:19"));
        System.out.println(mpgs);
    }

    MilesPerGallonInTimeFrame(List<String> rawLedger) {
        this.ledger = parseLedger(rawLedger);
    }

    Map<String, Map<String, SortedMap<LocalDateTime, GasConsumption>>> parseLedger(List<String> rawLedger) {
        Map<String, Map<String, SortedMap<LocalDateTime, GasConsumption>>> ledger = new HashMap<>();
        for (String ledgerLine : rawLedger) {
            LedgerEntry ledgerEntry = new LedgerEntry(ledgerLine);

            ledger.computeIfAbsent(ledgerEntry.name, v -> new HashMap<>());
            ledger.get(ledgerEntry.name).computeIfAbsent(ledgerEntry.car, v -> new TreeMap<>()); // tree map is sorted on date which implements comparable
            ledger.get(ledgerEntry.name).get(ledgerEntry.car).put(ledgerEntry.dateTime, new GasConsumption(ledgerEntry.milesDriven, ledgerEntry.gallons));
        }
        return ledger;
    }

    List<CarConsumption> milesPerGallon(String name, LocalDateTime start, LocalDateTime end) {
        List<CarConsumption> carsConsumption = new ArrayList<>();
        Set<String> cars = ledger.get(name).keySet();

        for (String car : cars) {
            SortedMap<LocalDateTime, GasConsumption> carConsumption = ledger.get(name).get(car).subMap(start, end);

            double miles = 0d;
            int gallons = 0;
            // we should be able to use stream.reduce instead, except that reduce in Java is worse designed than in javascript and one needs a PHD to use it
            for (GasConsumption consumption : carConsumption.values()) {
                miles += consumption.milesTravelled;
                gallons += consumption.gallons;
            }
            // some of his cars will not fall into the chosen time range
            if (!carConsumption.isEmpty()) {
                carsConsumption.add(new CarConsumption(car, miles / gallons));
            }
        }
        return carsConsumption;
    }

    static class LedgerEntry {
        String name;
        String car;
        double milesDriven;
        int gallons;
        LocalDateTime dateTime;

        public LedgerEntry(String ledgerLine) {
            String[] items = ledgerLine.split(",");
            name = items[0];
            car = items[1];
            milesDriven = Double.parseDouble(items[2]);
            gallons = Integer.parseInt(items[3]);
            dateTime = LocalDateTime.parse(items[4]);
        }
    }

    static class CarConsumption {
        String car;
        double mpg;

        public CarConsumption(String car, double mpg) {
            this.car = car;
            this.mpg = mpg;
        }

        @Override
        public String toString() {
            return "CarConsumption{" +
                    "car='" + car + '\'' +
                    ", mpg=" + mpg +
                    '}';
        }
    }

    static class GasConsumption {
        double milesTravelled;
        int gallons;

        public GasConsumption(double milesTravelled, int gallons) {
            this.milesTravelled = milesTravelled;
            this.gallons = gallons;
        }

        @Override
        public String toString() {
            return "GasConsumption{" +
                    "milesTravelled=" + milesTravelled +
                    ", gallons=" + gallons +
                    '}';
        }
    }
}
