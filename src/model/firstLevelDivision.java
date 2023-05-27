package model;

public class firstLevelDivision {
    private int divisionId;
    private String name;
    private int countryId;

    public firstLevelDivision(int divisionId, String name, int countryId) {
        this.divisionId = divisionId;
        this.name = name;
        this.countryId = countryId;
    }

    public int getDivisionId() {
        return this.divisionId;
    }

    public String getName() {
        return this.name;
    }

    public int getCountryId() {
        return this.countryId;
    }

    public String toString() {
        return this.divisionId + ": " + this.name;
    }
}
