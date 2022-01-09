package main.java.simulation.market;

import main.java.simulation.asset.AssetManager;
import main.java.simulation.asset.CurrencyData;
import main.java.simulation.asset.InformedSupplementaryAssetFactory;
import main.java.simulation.asset.RandomSupplementaryAssetFactory;
import main.java.simulation.holders.TradingEntitiesManager;
import main.java.simulation.util.RandomService;
import main.java.simulation.util.ResourceHolder;
import main.java.simulation.util.records.CurrencyRecord;

public class RandomMarketFactory implements MarketFactory {
    private static ResourceHolder resourceHolder;
    private final TradingEntitiesManager tradingEntitiesManager;
    private final AssetManager assetManager;

    public RandomMarketFactory(AssetManager assetManager, TradingEntitiesManager tradingEntitiesManager) {
        this.assetManager = assetManager;
        this.tradingEntitiesManager = tradingEntitiesManager;
    }

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
                var currencyData = this.assetManager.findCurrencyByName(currency.getName());
                if (currencyData == null) {
                    currencyID = new InformedSupplementaryAssetFactory(this.assetManager, currency.getName(), currency.getInitialRate()).createCurrencyAsset();
                    currencyData = (CurrencyData) this.assetManager.getAssetData(currencyID);
                    currencyData.addCountriesOfUse(currency.getCountriesOfUse());
                    currencyData.setStability(rand.yieldRandomGaussianNumber(0.01, 0.5));
                } else {
                    currencyID = currencyData.getUniqueIdentifyingName();
                }
                var newMarket = new StockMarket(this.assetManager, name, buyFee, sellFee, currencyID, address);
                var idx = new StockMarketIndex();
                for (int i = 0; i < initialNumberOfAssets; i++) {
                    idx.addCompany(this.tradingEntitiesManager.createNewCompany());
                }
                for (int i = 0; i < initialNumberOfAssets/2; i++) {
                    idx.addCompany(this.tradingEntitiesManager.createNewFund());
                }
                newMarket.addStockMarketIndex(idx);
                return newMarket;
            }
            case CURRENCIES_MARKET -> {
                var newMarket = new CurrenciesMarket(this.assetManager, name, buyFee, sellFee, address);
                for (int i = 0; i < initialNumberOfAssets; i++) {
                    newMarket.addNewAsset(new RandomSupplementaryAssetFactory(this.assetManager).createCurrencyAsset());
                }
                return newMarket;
            }
            case COMMODITIES_MARKET -> {
                var newMarket = new CommoditiesMarket(this.assetManager, name, buyFee, sellFee, address);
                for (int i = 0; i < initialNumberOfAssets; i++) {
                    newMarket.addNewAsset(new RandomSupplementaryAssetFactory(this.assetManager).createCommodityAsset());
                }
                return newMarket;
            }
            default -> throw new IllegalStateException();
        }
    }
}
