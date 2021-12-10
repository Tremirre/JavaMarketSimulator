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
    private final Random generator;

    private RandomDataGenerator() throws IOException {
        System.out.println("[LOGGING] Initializing RandomDataGenerator...");
        final String path = "resource\\address_data\\";
        var data = new HashMap<String, String[]>();
        String countries = Files.readString(Path.of(path + "countries.txt"));
        String[] countriesList = countries.split(";");
        for (String s : countriesList) {
            String cities = Files.readString(Path.of(path + s.toLowerCase() + ".txt"));
            String[] citiesList = cities.split(";");
            data.put(s, citiesList);
        }
        String allStreets = Files.readString(Path.of(path + "street_names.txt"));
        this.streets = allStreets.split(";");
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
        return streets[generator.nextInt(streets.length)];
    }

    public int yieldRandomInteger(int bound) {
        return this.generator.nextInt(bound);
    }

    public static RandomDataGenerator getInstance() throws IOException{
        if (instance == null) {
            instance = new RandomDataGenerator();
        }
        return instance;
    }
}