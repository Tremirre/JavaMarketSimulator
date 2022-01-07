package simulation.market;

import simulation.asset.AssetManager;
import simulation.asset.CurrencyData;
import simulation.asset.InformedSupplementaryAssetFactory;
import simulation.asset.RandomSupplementaryAssetFactory;
import simulation.holders.CompaniesManager;
import simulation.util.RandomService;
import simulation.util.ResourceHolder;
import simulation.util.records.CurrencyRecord;

public class RandomMarketFactory implements MarketFactory {
    private static ResourceHolder resourceHolder;

    public static void setFactoryResource(ResourceHolder resource) {
        resourceHolder = resource;
    }

    @Override
    public Market createMarket(MarketType type) {
        var rand = RandomService.getInstance();
        var name = (String) rand.sampleElement(resourceHolder.getMarketNames());
        var buyFee = 0.01 * rand.yieldRandomInteger(10) + 0.01;
        var sellFee = 0.01 * rand.yieldRandomInteger(10) + 0.01;
        var initialNumberOfAssets = rand.yieldRandomInteger(8) + 3;
        var address = rand.yieldRandomAddress(resourceHolder);
        switch (type) {
            case STOCK_MARKET -> {
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
                var newMarket = new StockMarket(name, buyFee, sellFee, currencyID, address);
                var companiesManager = CompaniesManager.getInstance();
                var idx = new StockMarketIndex();
                for (int i = 0; i < initialNumberOfAssets; i++) {
                    idx.addCompany(companiesManager.createNewCompany());
                }
                newMarket.addStockMarketIndex(idx);
                return newMarket;
            }
            case CURRENCIES_MARKET -> {
                var newMarket = new CurrenciesMarket(name, buyFee, sellFee, address);
                for (int i = 0; i < initialNumberOfAssets; i++) {
                    newMarket.addNewAsset(new RandomSupplementaryAssetFactory().createCurrencyAsset());
                }
                return newMarket;
            }
            case COMMODITIES_MARKET -> {
                var newMarket = new CommoditiesMarket(name, buyFee, sellFee, address);
                for (int i = 0; i < initialNumberOfAssets; i++) {
                    newMarket.addNewAsset(new RandomSupplementaryAssetFactory().createCommodityAsset());
                }
                return newMarket;
            }
            default -> throw new IllegalStateException();
        }
    }
}
