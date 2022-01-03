package simulation.asset;

import simulation.util.RandomService;

import java.util.HashSet;
import java.util.Arrays;

public class CurrencyData extends AssetData {
    private HashSet<String> countriesOfUse;
    private double stability;


    protected CurrencyData(int id, String name, double openingPrice, String[] countries, double stability) {
        super(id, name, openingPrice);
        this.countriesOfUse = new HashSet<>(Arrays.asList(countries));
        this.splittable = true;
        this.setStability(stability);
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

    public void setStability(double stability) {
        this.stability = Math.max(Math.min(stability, 1), 0);
    }

    @Override
    public double getQualityMeasure() {
        return this.stability;
    }

    public void processRandomEvent() {
        this.setStability(this.stability + RandomService.getInstance().yieldRandomGaussianNumber(0.01, 0));
    }
}
