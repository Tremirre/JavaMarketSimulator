package simulation.asset;

import simulation.util.RandomDataGenerator;

public class RandomNonDiscreteAssetFactory extends NonDiscreteAssetFactory {
    @Override
    public String createCurrencyAsset() {
        var rand = RandomDataGenerator.getInstance();
        var currencyName = rand.useCurrency();
        var rate = rand.yieldChosenCurrencyExchangeRate(currencyName);
        var countriesOfUse = rand.yieldChosenCurrencyCountriesOfUse(currencyName);
        return AssetManager.getInstance()
                .addCurrencyAsset(currencyName, rate, countriesOfUse.toArray(new String[0]))
                .getUniqueIdentifyingName();
    }

    @Override
    public String createCommodityAsset() {
        var rand = RandomDataGenerator.getInstance();
        return null;
        //AssetManager.getInstance().addCommodityAsset();
    }
}
