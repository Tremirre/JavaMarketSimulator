package simulation.holders;

import simulation.util.RandomDataGenerator;

import java.io.IOException;

public class Address {
    private String country;
    private String city;
    private String postalCode;
    private String streetName;
    private int buildingNumber;

    public Address(String country, String city, String postalCode, String streetName, int buildingNumber) {
        this.country = country;
        this.city = city;
        this.postalCode = postalCode;
        this.streetName = streetName;
        this.buildingNumber = buildingNumber;
    }

    public static Address getRandomAddress() throws IOException {
        var rand = RandomDataGenerator.getInstance();
        String country = rand.yieldCountry();
        return new Address(country,
                rand.yieldCityForCountry(country),
                rand.yieldPostCode(),
                rand.yieldStreetName(),
                rand.yieldRandomInteger(1000));
    }

    public void print() {
        System.out.printf("%s %d \n%s %s, %s \n",
                this.streetName,
                this.buildingNumber,
                this.postalCode,
                this.city,
                this.country);
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public int getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(int buildingNumber) {
        this.buildingNumber = buildingNumber;
    }
}
