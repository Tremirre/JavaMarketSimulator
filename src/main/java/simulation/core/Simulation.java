package simulation.core;

import simulation.asset.AssetManager;
import simulation.asset.RandomSupplementaryAssetFactory;
import simulation.holders.RandomHolderFactory;
import simulation.holders.TradingEntitiesManager;
import simulation.market.Market;
import simulation.market.MarketType;
import simulation.market.RandomMarketFactory;
import simulation.util.Constants;
import simulation.util.GlobalHoldersLock;
import simulation.util.ResourceHolder;

import java.io.IOException;
import java.util.HashSet;

public class Simulation {
    private final HashSet<Market> markets;
    private final AssetManager assetManager;
    private final TradingEntitiesManager tradingEntitiesManager;

    public Simulation() {
        this.markets = new HashSet<>();
        this.assetManager = new AssetManager();
        this.tradingEntitiesManager = new TradingEntitiesManager(this.assetManager, this.markets);
        this.setupDataResourceForRandomFactories();
        this.setupMarket(MarketType.STOCK_MARKET);
        this.setupMarket(MarketType.COMMODITIES_MARKET);
        this.setupMarket(MarketType.CURRENCIES_MARKET);
        this.setupInvestors();
    }

    public void runSimulationDay() {
        System.out.println("Processing simulation day");
        GlobalHoldersLock.writeUnlock();
        try {
            Thread.sleep(Constants.BASE_TRADING_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        GlobalHoldersLock.writeLock();
        for (var market : this.markets) {
            market.processAllOffers();
            market.updateOffers();
            market.removeOutdatedOffers();
        }
        /*if (day == 1) {
            SimulationConfig.getInstance().setBullProportion(0.65);
        } else if (day == Constants.YEAR) {
            SimulationConfig.getInstance().setBullProportion(0.35);
        } else if (day == Constants.YEAR + 100) {
            SimulationConfig.getInstance().setBullProportion(0.65);
        }*/
        this.assetManager.processEndDay();
        this.tradingEntitiesManager.processEndDay();
    }

    public void stop() {
        this.tradingEntitiesManager.stopEntities();
        GlobalHoldersLock.writeUnlock();
    }

    public void start() {
        this.tradingEntitiesManager.startEntities();
        GlobalHoldersLock.writeLock();
    }

    private void setupMarket(MarketType type) {
        this.markets.add(new RandomMarketFactory(this.assetManager, this.tradingEntitiesManager).createMarket(type));
    }

    private void setupInvestors() {
        for (int i = 0; i < 400; i++) {
            this.tradingEntitiesManager.createNewInvestor();
        }
    }

    private void setupDataResourceForRandomFactories() {
        var resourceHolder = new ResourceHolder();
        RandomHolderFactory.setFactoryResource(resourceHolder);
        RandomSupplementaryAssetFactory.setFactoryResource(resourceHolder);
        RandomMarketFactory.setFactoryResource(resourceHolder);
    }

    public AssetManager getAssetManager() {
        return this.assetManager;
    }

    public TradingEntitiesManager getEntitiesManager() { return this.tradingEntitiesManager; }
}
