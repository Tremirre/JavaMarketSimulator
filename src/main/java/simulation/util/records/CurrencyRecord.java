package simulation.util.records;

import java.util.HashSet;

/**
 * Class dedicated to holding a single currency
 */
public class CurrencyRecord extends Record {
    private HashSet<String> countriesOfUse = new HashSet<>();

    public CurrencyRecord(String name, double initialRate) {
        super(name, initialRate);
    }

    public void addCountry(String country) {
        this.countriesOfUse.add(country);
    }

    public HashSet<String> getCountriesOfUse() {
        return this.countriesOfUse;
    }
}
