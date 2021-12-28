package simulation.asset;

import java.util.HashSet;
import java.util.Arrays;

public class CurrencyData extends AssetData {
    private HashSet<String> countriesOfUse;

    protected CurrencyData(int id, String name, double openingPrice, String[] countries) {
        super(id, name, openingPrice);
        this.countriesOfUse = new HashSet<>(Arrays.asList(countries));
        this.splittable = true;
    }

    public void addCountryOfUse(String country) {
        this.countriesOfUse.add(country);
    }

    public void removeCountryOfUse(String country) {
        this.countriesOfUse.remove(country);
    }

    public HashSet<String> getCountriesOfUse() {
        return this.countriesOfUse;
    }
}
