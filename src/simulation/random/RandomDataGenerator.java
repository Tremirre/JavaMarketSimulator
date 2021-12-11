package simulation.random;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class RandomDataGenerator {
    private static RandomDataGenerator instance;
    private final HashMap<String, String[]> addressData;
    private final String[] streets;
    private final String[] names;
    private final String[] surnames;
    private final ArrayList<String> companyNames;
    private final Random generator;
    private static final int SEED = -1;

    private RandomDataGenerator() throws IOException {
        System.out.println("[LOGGING] Initializing RandomDataGenerator...");
        final String addressPath = "resource\\address_data\\";
        final String holdersPath = "resource\\holders_data\\";
        var data = new HashMap<String, String[]>();
        String countries = Files.readString(Path.of(addressPath + "countries.txt"));
        String[] countriesList = countries.split(";");
        for (String s : countriesList) {
            String cities = Files.readString(Path.of(addressPath + s.toLowerCase() + ".txt"));
            String[] citiesList = cities.split(";");
            data.put(s, citiesList);
        }
        this.addressData = data;
        String allStreets = Files.readString(Path.of(addressPath + "street_names.txt"));
        this.streets = allStreets.split(";");
        String allNames = Files.readString(Path.of(holdersPath + "names.txt"));
        this.names = allNames.split(";");
        String allSurnames = Files.readString(Path.of(holdersPath + "surnames.txt"));
        this.surnames = allSurnames.split(";");
        String allCompanyNames = Files.readString(Path.of(holdersPath + "companies.txt"));
        this.companyNames = new ArrayList<>(Arrays.asList(allCompanyNames.split(";")));
        this.generator = SEED >= 0 ? new Random(SEED) : new Random();
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

    public double yieldRandomNumber(double upperBound) {
        return this.generator.nextDouble()*upperBound;
    }

    public double yieldRandomGaussianNumber(double standardDeviation, double mean) {
        return this.generator.nextGaussian()*standardDeviation + mean;
    }

    public String yieldName() {
        return this.names[this.generator.nextInt(this.names.length)];
    }

    public String yieldSurname() {
        return this.surnames[this.generator.nextInt(this.surnames.length)];
    }

    public String yieldCompanyName() {
        var selected = this.companyNames.get(this.generator.nextInt(this.companyNames.size()));
        this.companyNames.remove(selected);
        return selected;
    }

    public Object sampleElement(Object[] array) {
        return array[this.generator.nextInt(array.length)];
    }

    public String yieldDate() {
        var month = this.generator.nextInt(12) + 1;
        var day = this.generator.nextInt(29) + 1;
        var year = this.generator.nextInt(41) + 1980;
        String monthD = month < 10 ? "0" : "";
        monthD += String.valueOf(month);
        String dayD = day < 10 ? "0" : "";
        dayD += String.valueOf(day);
        return String.valueOf(year) + '-' + monthD + '-' + dayD;
    }

    public static RandomDataGenerator getInstance() throws IOException{
        if (instance == null) {
            instance = new RandomDataGenerator();
        }
        return instance;
    }
}