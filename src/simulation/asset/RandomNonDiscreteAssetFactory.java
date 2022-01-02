package simulation.asset;

import simulation.util.RandomService;

public class RandomNonDiscreteAssetFactory extends NonDiscreteAssetFactory {
    @Override
    public String createCurrencyAsset() {
        var rand = RandomService.getInstance();
        var currencyName = rand.useCurrency();
        var rate = rand.yieldChosenCurrencyExchangeRate(currencyName);
        var countriesOfUse = rand.yieldChosenCurrencyCountriesOfUse(currencyName);
        var stability = rand.yieldRandomGaussianNumber(0.25, 0.5);
        return AssetManager.getInstance()
                .addCurrencyAsset(currencyName, rate, countriesOfUse.toArray(new String[0]), stability)
                .getUniqueIdentifyingName();
    }

    @Override
    public String createCommodityAsset() {
        var rand = RandomService.getInstance();
        var commodityName = rand.useCommodity();
        var rate = rand.yieldChosenCommodityExchangeRate(commodityName);
        var unit = rand.yieldChosenCommodityUnit(commodityName);
        return AssetManager.getInstance()
                .addCommodityAsset(commodityName, rate, unit, "US Dollar")
                .getUniqueIdentifyingName();
    }
}
