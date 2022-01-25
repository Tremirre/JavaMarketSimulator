package simulation.asset;

import simulation.util.RandomService;

import java.util.HashSet;

/**
 * Subclass of AssetData representing data of a currency asset.
 */
public class CurrencyData extends AssetData {
    /**
     * List of countries in which the currency is considered a legal tender.
     */
    private HashSet<String> countriesOfUse;
    /**
     * stability factor of the currency that defines its quality. This value can be only in the range
     * between 0 and 1.
     */
    private double stability;

    /**
     * Fills the asset with data.
     * @param id numerical id of the currency.
     * @param name name of the currency.
     * @param openingPrice initial price of the currency per 1 unit of it in DEFAULT STANDARD CURRENCY.
     * @param countries list of countries in which the currency is considered a legal tender.
     * @param stability stability factor of the currency that defines its quality.
     */
    protected CurrencyData(int id, String name, double openingPrice, HashSet<String> countries, double stability) {
        super(id, name, openingPrice);
        this.countriesOfUse = countries;
        this.splittable = true;
        this.setStability(stability);
    }

    public HashSet<String> getCountriesOfUse() {
        return this.countriesOfUse;
    }

    public void addCountriesOfUse(HashSet<String> countries) {
        this.countriesOfUse.addAll(countries);
    }

    /**
     * Sets currency stability ensuring it stays in range between 0 and 1.
     * @param stability new stability of the currency.
     */
    public void setStability(double stability) {
        this.stability = Math.max(Math.min(stability, 1), 0);
    }

    @Override
    public double getQualityMeasure() {
        return this.stability;
    }

    /**
     * {@inheritDoc}
     * In case of Currency Data it also randomly changes the stability value.
     */
    public void processAssetEvents() {
        super.processAssetEvents();
        this.setStability(this.stability + RandomService.getInstance().yieldRandomGaussianNumber(0.01, 0));
    }
}
