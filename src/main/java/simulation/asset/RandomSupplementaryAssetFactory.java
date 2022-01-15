package simulation.asset;

import simulation.util.RandomService;
import simulation.util.Resourced;
import simulation.util.records.CommodityRecord;
import simulation.util.records.CurrencyRecord;

public class RandomSupplementaryAssetFactory extends SupplementaryAssetFactory implements Resourced {

    public RandomSupplementaryAssetFactory(AssetManager assetManager) {
        super(assetManager);
    }

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

    @Override
    public String createCommodityAsset() {
        var rand = RandomService.getInstance();
        var commodity = (CommodityRecord) rand.sampleElement(resourceHolder.getUnusedCommodities().toArray());
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
