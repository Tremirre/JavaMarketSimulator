package simulation.market;

import simulation.address.RandomAddressFactory;
import simulation.asset.*;
import simulation.holders.RandomHolderFactory;
import simulation.holders.TradingEntitiesManager;
import simulation.util.RandomService;
import simulation.util.Resourced;
import simulation.util.records.CurrencyRecord;

/**
 * Class that creates new markets with random data and assets.
 */
public class RandomMarketFactory extends MarketFactory implements Resourced {
    private final TradingEntitiesManager tradingEntitiesManager;

    /**
     * @param assetManager reference to asset manager.
     * @param tradingEntitiesManager reference to entities manager.
     */
    public RandomMarketFactory(AssetManager assetManager, TradingEntitiesManager tradingEntitiesManager) {
        super(assetManager);
        this.tradingEntitiesManager = tradingEntitiesManager;
    }

    /**
     * Creates new market based on the category.
     * Creates new assets corresponding to the market category and makes them available on the new market.
     * @param category market category.
     * @return new market with random parameters and assets.
     */
    @Override
    public Market createMarket(AssetCategory category) {
        var rand = RandomService.getInstance();
        var name = (String) rand.sampleElement(resourceHolder.getMarketNames());
        var buyFee = 0.01 * rand.yieldRandomInteger(10) + 0.01;
        var sellFee = 0.01 * rand.yieldRandomInteger(10) + 0.01;
        var initialNumberOfAssets = rand.yieldRandomInteger(8) + 3;
        var address = new RandomAddressFactory().createAddress();
        switch (category) {
            case STOCK -> {
                var currency = (CurrencyRecord) rand.sampleElement(resourceHolder.getCurrencies().toArray());
                var factory = new RandomHolderFactory(
                        this.tradingEntitiesManager.getTotalNumberOfEntities(),
                        this.assetManager
                );
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
                var idx = this.tradingEntitiesManager.createNewStockIndex(
                        rand.yieldRandomString(4).toUpperCase() + (rand.yieldRandomInteger(100) + 10)
                );
                for (int i = 0; i < initialNumberOfAssets; i++) {
                    var company = this.tradingEntitiesManager.createNewCompany(factory);
                    idx.addCompany(company);
                }
                newMarket.addStockMarketIndex(idx);
                return newMarket;
            }
            case CURRENCY -> {
                var newMarket = new CurrenciesMarket(this.assetManager, name, buyFee, sellFee, address);
                for (int i = 0; i < initialNumberOfAssets; i++) {
                    var currency = new RandomSupplementaryAssetFactory(this.assetManager).createCurrencyAsset();
                    if (currency == null) {
                        currency = (String) rand.sampleElement(
                                this.assetManager.getAssetsByCategory(AssetCategory.CURRENCY).toArray()
                        );
                    }
                    newMarket.addNewAsset(currency);
                }
                return newMarket;
            }
            case COMMODITY -> {
                var newMarket = new CommoditiesMarket(this.assetManager, name, buyFee, sellFee, address);
                for (int i = 0; i < initialNumberOfAssets; i++) {
                    var asset = new RandomSupplementaryAssetFactory(this.assetManager).createCommodityAsset();
                    if (asset == null)
                        asset = (String) rand.sampleElement(
                                this.assetManager.getAssetsByCategory(AssetCategory.COMMODITY).toArray()
                        );
                    newMarket.addNewAsset(asset);
                }
                return newMarket;
            }
            default -> throw new IllegalStateException();
        }
    }
}
