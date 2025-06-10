package org.haldokan.edge.lunchbox.misc;


import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

public class JdkStreamTest {

    private static List<Mitarbeiter> arbeiter = new ArrayList<>();
    private static int grosse = 50;
    private static Random rand = new Random();
    private static String[] abteilungen = new String[]{"Technology", "Arbeitsstaerke", "Herstellung", "Verwaltung"};

    
    public static void bereiten() {
        for (int i = 0; i < grosse; i++) {
            Geschlecht g = rand.nextInt(100) % 2 == 0 ? Geschlecht.Mann : Geschlecht.Frau;
            arbeiter.add(new Mitarbeiter(Double.valueOf(rand.nextInt(200_000) + Math.random()), abteilungen[rand
                    .nextInt(abteilungen.length)], "Name" + i, g));
        }
    }

    @Test
    public void frauArberiter() {
        arbeiter.stream().forEach((Mitarbeiter a) -> {
            if (a.getGeschlecht() == Geschlecht.Frau)
                System.out.println("Frauarbeiterinnen: " + a);
        });
    }

    @Test
    public void geschaeftsAufwand() {
        double aufwand = arbeiter.stream().collect(Collectors.summingDouble(Mitarbeiter::getGehalt));
        System.out.println("Aufwand: " + aufwand);
    }

    @Test
    public void geschaeftsAufwandFuerFrauen() {
        double aufwand = arbeiter.stream().filter(a -> a.getGeschlecht() == Geschlecht.Frau)
                .collect(Collectors.summingDouble(Mitarbeiter::getGehalt));
        System.out.println("Aufwand: " + aufwand);
    }

    @Test
    public void arbeiterProAbteilung() {
        Map<String, List<Mitarbeiter>> arbeiterProAbteilung = arbeiter.stream().collect(
                Collectors.groupingBy(Mitarbeiter::getAbteilung));
        System.out.println("Arbeiter pro Abteilung: " + arbeiterProAbteilung);
    }

    @Test
    public void aufwandProAbteilung() {
        Map<String, Double> aufwandProAbteilung = arbeiter.stream().collect(
                Collectors.groupingBy(Mitarbeiter::getAbteilung, Collectors.summingDouble(Mitarbeiter::getGehalt)));
        System.out.println("Aufwand pro Abteilung: " + aufwandProAbteilung);
    }

    @Test
    public void aufwandProAbteilungUndGeschlecht() {
        Map<String, Double> aufwandProAbteilungUndGeschlecht = arbeiter.stream().collect(
                Collectors.groupingBy(a -> a.getAbteilung() + "/" + a.getGeschlecht(),
                        Collectors.summingDouble(Mitarbeiter::getGehalt)));
        System.out.println("Aufwand pro Abteilung und Geschlecht: " + aufwandProAbteilungUndGeschlecht);
    }

    @Test
    public void maximalAufwandUnterAbteilungen() {
        Optional<Map.Entry<String, Double>> maximalAufwandUnterAbteilungen = arbeiter
                .stream()
                .collect(
                        Collectors.groupingBy(Mitarbeiter::getAbteilung,
                                Collectors.summingDouble(Mitarbeiter::getGehalt))).entrySet().stream()
                .collect(Collectors.maxBy(Map.Entry.comparingByValue()));
        System.out.println("Maximal Aufwand unter Abteilungen: " + maximalAufwandUnterAbteilungen.get());
    }

    @Test
    public void arbeiterMitGrossteGehalt() {
        Optional<Mitarbeiter> arbeiterMitMaximalGehalt = arbeiter.stream().collect(
                Collectors.maxBy((a, b) -> a.getGehalt().compareTo(b.getGehalt())));
        System.out.println("Arbeiter mit maximal Gehalt: " + arbeiterMitMaximalGehalt.get());
    }

    @Test
    public void gehaltOrdnen() {
        Set<Double> sortierendArbeiterListe = arbeiter.stream().map(Mitarbeiter::getGehalt)
                .collect(Collectors.toCollection(TreeSet::new));
        System.out.println("Sortierte GehaltListe: " + sortierendArbeiterListe);
    }

    @Test
    public void arbeiterBeiGehaltOrdnen() {
        List<Mitarbeiter> sortierendArbeiterListe = arbeiter.stream()
                .sorted((a, b) -> a.getGehalt().compareTo(b.getGehalt())).collect(Collectors.toList());
        System.out.println("Arbeiter Liste bei Gehalt sortierte: " + sortierendArbeiterListe);
    }

    @Test
    public void abteilArbeiterUnterArmUndReich() {
        Double armReichGrenze = 50_000d;
        Map<Boolean, List<Mitarbeiter>> armUndReich = arbeiter.stream().collect(
                Collectors.partitioningBy(a -> a.getGehalt() > armReichGrenze));
        System.out.println("Arme und Reiche: " + armUndReich);

        // Es gibt eine Fehle mit Ordnung wen die ueber 'string' Vergleichung
        // basiert
        List<String> dieReiche = armUndReich.entrySet().stream().filter(Map.Entry::getKey)
                .flatMap(e -> e.getValue().stream()).map(e -> e.getGehalt() + "/" + e.getName()).sorted()
                .collect(Collectors.toList());
        System.out.println("Die Reiche: " + dieReiche);

        // Ebenso here gibt es eine Fehle mit Ordnung wen die ueber 'string'
        // Vergleichung basiert
        List<String> dieArme = armUndReich.entrySet().stream().filter(e -> !e.getKey())
                .flatMap(e -> e.getValue().stream()).map(e -> e.getGehalt() + "/" + e.getName()).sorted()
                .collect(Collectors.toList());
        System.out.println("Die Arme: " + dieArme);

    }
}
