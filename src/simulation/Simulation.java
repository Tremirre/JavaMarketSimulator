package simulation;

import simulation.asset.AssetManager;
import simulation.asset.RandomSupplementaryAssetFactory;
import simulation.holders.TradingEntitiesManager;
import simulation.holders.RandomHolderFactory;
import simulation.market.*;
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
        this.start();
    }

    public void runSimulationDay(int day) {
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
        GlobalHoldersLock.writeUnlock();
    }

    public void stop() {
        this.tradingEntitiesManager.stopEntities();
    }

    private void start() {
        this.tradingEntitiesManager.startEntities();
    }

    private void setupMarket(MarketType type) {
        this.markets.add(new RandomMarketFactory(this.assetManager, this.tradingEntitiesManager).createMarket(type));
    }

    public void pause() {
        GlobalHoldersLock.writeLock();
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        GlobalHoldersLock.writeUnlock();
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

    public AssetManager getAssetManager() { return this.assetManager; }
}
