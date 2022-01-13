package simulation.market;

import simulation.address.RandomAddressFactory;
import simulation.asset.*;
import simulation.holders.TradingEntitiesManager;
import simulation.util.RandomService;
import simulation.util.ResourceHolder;
import simulation.util.Resourced;
import simulation.util.records.CurrencyRecord;

public class RandomMarketFactory extends MarketFactory implements Resourced {
    private final TradingEntitiesManager tradingEntitiesManager;

    public RandomMarketFactory(AssetManager assetManager, TradingEntitiesManager tradingEntitiesManager) {
        super(assetManager);
        this.tradingEntitiesManager = tradingEntitiesManager;
    }

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
                for (int i = 0; i < initialNumberOfAssets / 2; i++) {
                    idx.addCompany(this.tradingEntitiesManager.createNewFund());
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
                    newMarket.addNewAsset(new RandomSupplementaryAssetFactory(this.assetManager).createCommodityAsset());
                }
                return newMarket;
            }
            default -> throw new IllegalStateException();
        }
    }
}
