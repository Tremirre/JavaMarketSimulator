package simulation.core;

import simulation.asset.AssetCategory;
import simulation.asset.AssetManager;
import simulation.holders.TradingEntitiesManager;
import simulation.market.Market;
import simulation.market.RandomMarketFactory;
import simulation.util.Constants;
import simulation.util.GlobalHoldersLock;
import simulation.util.Resourced;

import java.util.HashSet;

public class Simulation implements Resourced {
    private final HashSet<Market> markets;
    private final AssetManager assetManager;
    private final TradingEntitiesManager tradingEntitiesManager;

    public Simulation() {
        this.resourceHolder.refresh();
        this.markets = new HashSet<>();
        this.assetManager = new AssetManager();
        this.tradingEntitiesManager = new TradingEntitiesManager(this.assetManager, this.markets);
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

    public void setupRandomMarket(AssetCategory category) {
        this.addNewMarket(new RandomMarketFactory(this.assetManager, this.tradingEntitiesManager).createMarket(category));
    }

    public void addNewMarket(Market market) {
        this.markets.add(market);
        this.tradingEntitiesManager.autoCreateInvestors();
    }

    public AssetManager getAssetManager() {
        return this.assetManager;
    }

    public TradingEntitiesManager getEntitiesManager() { return this.tradingEntitiesManager; }

    public HashSet<Market> getMarkets() {
        return this.markets;
    }

    public Market getMarketByName(String name) {
        for (var market : this.markets) {
            if (market.getName().equals(name)) {
                return market;
            }
        }
        throw new IllegalArgumentException("Invalid market name: " + name);
    }

    public boolean doesMarketExist(String name) {
        for (var market : this.markets) {
            if (market.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
