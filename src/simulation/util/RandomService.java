package simulation.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class RandomService {
    private static RandomService instance;
    private final HashMap<String, String[]> citiesCountryMapping;
    private final HashMap<String, HashSet<String>> countriesCurrencyMapping;
    private final HashMap<String, Double> currenciesRates;
    private final ArrayList<String> unusedCurrencies;
    private String[] streets;
    private String[] names;
    private String[] surnames;
    private ArrayList<String> companyNames;
    private final Random generator;
    private static final int SEED = -1;

    private RandomService() {
        System.out.println("[LOGGING] Initializing RandomDataGenerator...");
        final String addressPath = "resource\\address_data\\";
        final String holdersPath = "resource\\holders_data\\";
        this.citiesCountryMapping = new HashMap<>();
        this.countriesCurrencyMapping = new HashMap<>();
        this.currenciesRates = new HashMap<>();
        this.unusedCurrencies = new ArrayList<>();
        try {
            String countriesCurrencies = Files.readString(Path.of(addressPath + "countries_currencies.txt"));
            String[] currencyRecords = countriesCurrencies.split("\n");
            ArrayList<String> countriesList = new ArrayList<>();
            for (var record : currencyRecords) {
                String[] unpackedRecord = record.split(";");
                var country = unpackedRecord[0];
                var currency = unpackedRecord[1];
                var rate = Double.parseDouble(unpackedRecord[2]);
                countriesList.add(country);
                this.currenciesRates.put(currency, rate);
                if (!this.countriesCurrencyMapping.containsKey(currency)) {
                    this.countriesCurrencyMapping.put(currency, new HashSet<>());
                }
                this.countriesCurrencyMapping.get(currency).add(country);
            }
            this.unusedCurrencies.addAll(this.countriesCurrencyMapping.keySet());
            for (String country : countriesList) {
                String cities = Files.readString(Path.of(addressPath + country.toLowerCase() + ".txt"));
                String[] citiesList = cities.split(";");
                citiesCountryMapping.put(country, citiesList);
            }
            String allStreets = Files.readString(Path.of(addressPath + "street_names.txt"));
            this.streets = allStreets.split(";");
            String allNames = Files.readString(Path.of(holdersPath + "names.txt"));
            this.names = allNames.split(";");
            String allSurnames = Files.readString(Path.of(holdersPath + "surnames.txt"));
            this.surnames = allSurnames.split(";");
            String allCompanyNames = Files.readString(Path.of(holdersPath + "companies.txt"));
            this.companyNames = new ArrayList<>(Arrays.asList(allCompanyNames.split(";")));
        } catch (IOException e) {
            System.out.println("Failed to load a resource!");
            System.out.println(e.getMessage());
            System.exit(1);
        }
        this.generator = SEED >= 0 ? new Random(SEED) : new Random();
    }

    public String yieldCountry() {
        var keys = this.citiesCountryMapping.keySet();
        String[] countries = keys.toArray(new String[0]);
        int randomIdx = generator.nextInt(countries.length);
        return countries[randomIdx];
    }

    public String yieldCityForCountry(String country) {
        if (!this.citiesCountryMapping.containsKey(country))
            return null;
        String[] cities = this.citiesCountryMapping.get(country);
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

    public String useCompanyName() {
        return this.takeUsableResource(this.companyNames);
    }

    public String useCurrency() {
        return this.takeUsableResource(this.unusedCurrencies);
    }

    public HashSet<String> yieldChosenCurrencyCountriesOfUse(String currency) {
        return this.countriesCurrencyMapping.get(currency);
    }

    public double yieldChosenCurrencyExchangeRate(String currency) {
        return this.currenciesRates.get(currency);
    }

    private String takeUsableResource(ArrayList<String> resource) {
        if (resource.size() == 0)
            return null;
        var selected = (String) this.sampleElement(resource.toArray());
        resource.remove(selected);
        return selected;
    }

    public String yieldRandomString(int length) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append((char) (this.generator.nextInt(26) + 97));
        }
        return result.toString();
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

    public static RandomService getInstance() {
        if (instance == null) {
            instance = new RandomService();
        }
        return instance;
    }
}