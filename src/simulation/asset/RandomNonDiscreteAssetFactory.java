package simulation.asset;

import simulation.util.RandomService;

public class RandomNonDiscreteAssetFactory extends NonDiscreteAssetFactory {
    @Override
    public String createCurrencyAsset() {
        var rand = RandomService.getInstance();
        var currencyName = rand.useCurrency();
        var rate = rand.yieldChosenCurrencyExchangeRate(currencyName);
        var countriesOfUse = rand.yieldChosenCurrencyCountriesOfUse(currencyName);
        return AssetManager.getInstance()
                .addCurrencyAsset(currencyName, rate, countriesOfUse.toArray(new String[0]))
                .getUniqueIdentifyingName();
    }

    @Override
    public String createCommodityAsset() {
        var rand = RandomService.getInstance();
        return null;
        //AssetManager.getInstance().addCommodityAsset();
    }
}
