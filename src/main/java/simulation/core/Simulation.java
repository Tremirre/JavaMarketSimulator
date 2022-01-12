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
        this.setupRandomMarket(MarketType.CURRENCIES_MARKET, false);
        this.setupRandomMarket(MarketType.COMMODITIES_MARKET, false);
        this.setupRandomMarket(MarketType.STOCK_MARKET, false);
    }

    public void runSimulationDay() {
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

    private void setupRandomMarket(MarketType type, boolean runNewInvestors) {
        this.addNewMarket(new RandomMarketFactory(this.assetManager, this.tradingEntitiesManager).createMarket(type),
                runNewInvestors);
    }

    public void addNewMarket(Market market, boolean runNewInvestors) {
        this.markets.add(market);
        this.tradingEntitiesManager.autoCreateInvestors(runNewInvestors);
    }

    private void setupInvestors() {
        for (int i = 0; i < 400; i++) {
            this.tradingEntitiesManager.createNewInvestor();
        }
    }

    public AssetManager getAssetManager() {
        return this.assetManager;
    }

    public TradingEntitiesManager getEntitiesManager() { return this.tradingEntitiesManager; }
}
