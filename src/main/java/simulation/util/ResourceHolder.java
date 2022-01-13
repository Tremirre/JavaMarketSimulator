package simulation.util;

import simulation.util.records.CommodityRecord;
import simulation.util.records.CurrencyRecord;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ResourceHolder {
    private final static String simulationResourcePath = "src\\main\\resources\\simulation_data";
    private final static String addressPath = simulationResourcePath + "\\address_data\\";
    private final static String customNamesPath = simulationResourcePath + "\\custom_names\\";
    private final static String assetPath = simulationResourcePath + "\\asset_data\\";

    private final HashMap<String, String[]> citiesCountryMapping;
    private final ArrayList<CurrencyRecord> currencies;
    private final ArrayList<CommodityRecord> commodities;

    private String[] streets;
    private String[] names;
    private String[] surnames;
    private String[] marketNames;
    private ArrayList<String> companyNames;

    private void loadCurrenciesAndCountries() throws IOException {
        String countriesCurrencies = Files.readString(Path.of(assetPath + "countries_currencies.txt"));
        String[] currencyRecords = countriesCurrencies.split("\n");
        for (var record : currencyRecords) {
            String[] unpackedRecord = record.split(";");
            var country = unpackedRecord[0];
            var currencyName = unpackedRecord[1];
            var currencyRate = Double.parseDouble(unpackedRecord[2]);
            citiesCountryMapping.put(country, null);
            boolean currencyFound = false;
            for (var storedRecord : currencies) {
                if (storedRecord.getName().equals(currencyName)) {
                    storedRecord.addCountry(country);
                    currencyFound = true;
                    break;
                }
            }
            if (currencyFound)
                continue;
            var newCurrency = new CurrencyRecord(currencyName, currencyRate);
            newCurrency.addCountry(country);
            this.currencies.add(newCurrency);
        }
    }

    private void loadAddressData() throws IOException {
        for (String country : this.citiesCountryMapping.keySet()) {
            String cities = Files.readString(Path.of(addressPath + country.toLowerCase() + ".txt"));
            String[] citiesList = cities.split(";");
            citiesCountryMapping.put(country, citiesList);
        }
        String allStreets = Files.readString(Path.of(addressPath + "street_names.txt"));
        this.streets = allStreets.split(";");
    }

    private void loadCommodities() throws IOException {
        String commodities = Files.readString(Path.of(assetPath + "commodity_data.txt"));
        String[] commoditiesRecords = commodities.split("\n");
        for (String record : commoditiesRecords) {
            String[] unpackedRecord = record.split(";");
            var commodityName = unpackedRecord[0];
            var commodityUnit = unpackedRecord[1];
            var commodityRate = Double.parseDouble(unpackedRecord[2]);
            this.commodities.add(new CommodityRecord(commodityName, commodityRate, commodityUnit));
        }
    }

    private void loadCustomNamesData() throws IOException {
        String allNames = Files.readString(Path.of(customNamesPath + "names.txt"));
        this.names = allNames.split(";");
        String allSurnames = Files.readString(Path.of(customNamesPath + "surnames.txt"));
        this.surnames = allSurnames.split(";");
        String allCompanyNames = Files.readString(Path.of(customNamesPath + "company_names.txt"));
        this.companyNames = new ArrayList<>(Arrays.asList(allCompanyNames.split(";")));
        String allMarketNames = Files.readString(Path.of(customNamesPath + "market_names.txt"));
        this.marketNames = allMarketNames.split(";");
    }

    public ResourceHolder() {
        this.citiesCountryMapping = new HashMap<>();
        this.currencies = new ArrayList<>();
        this.commodities = new ArrayList<>();
        try {
            this.loadCurrenciesAndCountries();
            this.loadAddressData();
            this.loadCommodities();
            this.loadCustomNamesData();
        } catch (IOException e) {
            System.out.println("Failed to load a resource!");
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public HashMap<String, String[]> getCitiesCountryMapping() {
        return citiesCountryMapping;
    }

    public ArrayList<CurrencyRecord> getCurrencies() {
        return currencies;
    }

    public ArrayList<CurrencyRecord> getUnusedCurrencies() {
        var result = new ArrayList<CurrencyRecord>();
        for (var cur : this.currencies) {
            if (!cur.isUsed()) {
                result.add(cur);
            }
        }
        return result;
    }

    public ArrayList<CommodityRecord> getCommodities() {
        return commodities;
    }

    public ArrayList<CommodityRecord> getUnusedCommodities() {
        var result = new ArrayList<CommodityRecord>();
        for (var com : this.commodities) {
            if (!com.isUsed()) {
                result.add(com);
            }
        }
        return result;
    }

    public String[] getCountries() {
        return this.citiesCountryMapping.keySet().toArray(new String[0]);
    }

    public String[] getCitiesForCountry(String country) {
        return this.citiesCountryMapping.get(country);
    }

    public String[] getStreets() {
        return streets;
    }

    public String[] getNames() {
        return names;
    }

    public String[] getSurnames() {
        return surnames;
    }

    public String[] getMarketNames() {
        return this.marketNames;
    }

    public ArrayList<String> getCompanyNames() {
        return companyNames;
    }

    public void removeCompanyName(String name) {
        this.companyNames.remove(name);
    }

    public void refresh() {
        for (var currency : this.currencies)
            currency.unUse();
        for (var commodity : this.commodities)
            commodity.unUse();
    }
}
