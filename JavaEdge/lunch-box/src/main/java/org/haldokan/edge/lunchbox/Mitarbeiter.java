package org.haldokan.edge.lunchbox;

public class Mitarbeiter {
    private final Double gehalt;
    private final String abteilung;
    private final String name;
    private final Geschlecht geschlecht;

    public Mitarbeiter(Double gehalt, String abteilung, String name, Geschlecht geschlecht) {
        this.gehalt = gehalt;
        this.abteilung = abteilung;
        this.name = name;
        this.geschlecht = geschlecht;
    }

    public Double getGehalt() {
        return gehalt;
    }

    public String getAbteilung() {
        return abteilung;
    }

    public String getName() {
        return name;
    }

    public Geschlecht getGeschlecht() {
        return geschlecht;
    }

    @Override
    public String toString() {
        return "Mitarbeiter [gehalt=" + gehalt + ", abteilung=" + abteilung + ", name=" + name + ", geschlecht="
                + geschlecht + "]";
    }
}