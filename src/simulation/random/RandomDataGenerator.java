package simulation.random;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Random;

public class RandomDataGenerator {
    private static RandomDataGenerator instance;
    private final HashMap<String, String[]> addressData;
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

    public static RandomDataGenerator getInstance() throws IOException{
        if (instance == null) {
            instance = new RandomDataGenerator();
        }
        return instance;
    }
}