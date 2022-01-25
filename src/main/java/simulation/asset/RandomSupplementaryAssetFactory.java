package simulation.asset;

import simulation.util.RandomService;
import simulation.util.Resourced;
import simulation.util.records.CommodityRecord;
import simulation.util.records.CurrencyRecord;

/**
 * This class represents objects that create new assets (not associated with any trading entity) with random data.
 */
public class RandomSupplementaryAssetFactory extends SupplementaryAssetFactory implements Resourced {

    /**
     * Passes the reference to the asset manager.
     * @param assetManager reference to the asset manager.
     */
    public RandomSupplementaryAssetFactory(AssetManager assetManager) {
        super(assetManager);
    }

    /**
     * {@inheritDoc
     * Creates currency based on the random currency record in the resource holder. If there are no more usable
     * currency records, returns null.
     * Stability is generated at random.
     * @return
     */
    @Override
    public String createCurrencyAsset() {
        var rand = RandomService.getInstance();
        var currency = (CurrencyRecord) rand.sampleElement(resourceHolder.getUnusedCurrencies().toArray());
        if (currency == null) {
            return null;
        }
        currency.use();
        var rate = currency.getInitialRate();
        var countriesOfUse = currency.getCountriesOfUse();
        var stability = rand.yieldRandomGaussianNumber(0.25, 0.5);
        return this.assetManager
                .addCurrencyAsset(currency.getName(), rate, countriesOfUse, stability)
                .getUniqueIdentifyingName();
    }

    /**
     * {@inheritDoc}
     * Creates commodity based on the random commodity record in the resource holder. If there are no more
     * usable commodity records, returns null.
     * Trading Currency sampled at random from the resource holder, if not present in the simulation it is generated
     * along the new commodity.
     * @return
     */
    @Override
    public String createCommodityAsset() {
        var rand = RandomService.getInstance();
        var commodity = (CommodityRecord) rand.sampleElement(resourceHolder.getUnusedCommodities().toArray());
        if (commodity == null) {
            return null;
        }
        commodity.use();
        var rate = commodity.getInitialRate();
        var unit = commodity.getUnit();
        var currency = (CurrencyRecord) rand.sampleElement(resourceHolder.getCurrencies().toArray());
        currency.use();
        String currencyID;
        var currencyData = this.assetManager.findCurrencyByName(currency.getName());
        if (currencyData == null) {
            currencyID = new InformedSupplementaryAssetFactory(this.assetManager,
                    currency.getName(),
                    currency.getInitialRate())
                    .createCurrencyAsset();
            currencyData = (CurrencyData) this.assetManager.getAssetData(currencyID);
            currencyData.addCountriesOfUse(currency.getCountriesOfUse());
            currencyData.setStability(rand.yieldRandomGaussianNumber(0.2, 0.5));
        } else {
            currencyID = currencyData.getUniqueIdentifyingName();
        }
        return this.assetManager
                .addCommodityAsset(commodity.getName(), rate, unit, currencyID)
                .getUniqueIdentifyingName();
    }
}
