package model;

public class country {
    private int countryId;
    private String name;

    public country(int countryId, String name) {
        this.countryId = countryId;
        this.name = name;
    }

    public int getCountryId() {
        return this.countryId;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.countryId + ": " + this.name;
    }
}
