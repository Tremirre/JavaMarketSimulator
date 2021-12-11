package simulation.random;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Random;

public class RandomDataGenerator {
    private static RandomDataGenerator instance;
    private final HashMap<String, String[]> addressData;
    private final String[] streets;
    private final String[] names;
    private final String[] surnames;
    private final Random generator;

    private RandomDataGenerator() throws IOException {
        System.out.println("[LOGGING] Initializing RandomDataGenerator...");
        final String addressPath = "resource\\address_data\\";
        final String credentialsPath = "resource\\credentials_data\\";
        var data = new HashMap<String, String[]>();
        String countries = Files.readString(Path.of(addressPath + "countries.txt"));
        String[] countriesList = countries.split(";");
        for (String s : countriesList) {
            String cities = Files.readString(Path.of(addressPath + s.toLowerCase() + ".txt"));
            String[] citiesList = cities.split(";");
            data.put(s, citiesList);
        }
        String allStreets = Files.readString(Path.of(addressPath + "street_names.txt"));
        this.streets = allStreets.split(";");
        String allNames = Files.readString(Path.of(credentialsPath + "names.txt"));
        this.names = allNames.split(";");
        String allSurnames = Files.readString(Path.of(credentialsPath + "surnames.txt"));
        this.surnames = allSurnames.split(";");
        this.addressData = data;
        this.generator = new Random();
    }

    public String yieldCountry() {
        var keys = this.addressData.keySet();
        String[] countries = keys.toArray(new String[0]);
        int randomIdx = generator.nextInt(countries.length);
        return countries[randomIdx];
    }

    public String yieldCityForCountry(String country) {
        if (!this.addressData.containsKey(country))
            return null;
        String[] cities = this.addressData.get(country);
        int randomIdx = generator.nextInt(cities.length);
        return cities[randomIdx];
    }

    public String yieldPostCode() {
        String[] randomValues = new String[5];
        for (int i = 0; i < randomValues.length; i++)
            randomValues[i] = String.valueOf(this.generator.nextInt(10));
        return randomValues[0] + randomValues[1] + '-' + randomValues[2] + randomValues[3] + randomValues[4];
    }

    public String yieldStreetName() {
        return this.streets[generator.nextInt(streets.length)];
    }

    public int yieldRandomInteger(int bound) {
        return this.generator.nextInt(bound);
    }

    public String yieldName() {
        return this.names[this.generator.nextInt(this.names.length)];
    }

    public String yieldSurname() {
        return this.surnames[this.generator.nextInt(this.surnames.length)];
    }

    public static RandomDataGenerator getInstance() throws IOException{
        if (instance == null) {
            instance = new RandomDataGenerator();
        }
        return instance;
    }
}