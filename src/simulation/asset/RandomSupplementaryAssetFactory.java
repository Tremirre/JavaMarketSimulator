package simulation.asset;

import simulation.util.RandomService;
import simulation.util.ResourceHolder;
import simulation.util.records.CommodityRecord;
import simulation.util.records.CurrencyRecord;

public class RandomSupplementaryAssetFactory extends SupplementaryAssetFactory {
    private static ResourceHolder resourceHolder;

    public static void setFactoryResource(ResourceHolder resource) {
        resourceHolder = resource;
    }

    @Override
    public String createCurrencyAsset() {
        var rand = RandomService.getInstance();
        var currency = (CurrencyRecord) rand.sampleElement(resourceHolder.getUnusedCurrencies().toArray());
        if (currency == null) {
            throw new RuntimeException("Run out of free random currencies");
        }
        currency.use();
        var rate = currency.getInitialRate();
        var countriesOfUse = currency.getCountriesOfUse();
        var stability = rand.yieldRandomGaussianNumber(0.25, 0.5);
        return AssetManager.getInstance()
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
        if (currency == null) {
            throw new RuntimeException("Run out of free random currencies");
        }
        currency.use();
        String currencyID;
        var currencyData = AssetManager.getInstance().findCurrencyByName(currency.getName());
        if (currencyData == null) {
            currencyID = new InformedSupplementaryAssetFactory(currency.getName(), currency.getInitialRate()).createCurrencyAsset();
            currencyData = (CurrencyData) AssetManager.getInstance().getAssetData(currencyID);
            currencyData.addCountriesOfUse(currency.getCountriesOfUse());
            currencyData.setStability(rand.yieldRandomGaussianNumber(0.01, 0.5));
        } else {
            currencyID = currencyData.getUniqueIdentifyingName();
        }
        return AssetManager.getInstance()
                .addCommodityAsset(commodity.getName(), rate, unit, currencyID)
                .getUniqueIdentifyingName();
    }
}
