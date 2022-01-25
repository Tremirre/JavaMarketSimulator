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

/**
 * Class that binds all simulation parts together and allows for controlling the course of the simulation.
 */
public class Simulation implements Resourced {
    /**
     * Set of markets existing in the simulation.
     */
    private final HashSet<Market> markets;
    /**
     * Entity taking care of asset management.
     */
    private final AssetManager assetManager;
    /**
     * Entity taking care of holders management.
     */
    private final TradingEntitiesManager tradingEntitiesManager;
    /**
     * Flag denoting whether the simulation has started.
     */
    private boolean started = false;
    /**
     * Day counter.
     */
    private int day = 0;

    /**
     * Initializes the markets' container, managers and refreshes the resource holder.
     */
    public Simulation() {
        this.resourceHolder.refresh();
        this.markets = new HashSet<>();
        this.assetManager = new AssetManager();
        this.tradingEntitiesManager = new TradingEntitiesManager(this.assetManager, this.markets);
    }

    /**
     * Runs a single simulation day:
     *  - increments day counter,
     *  - enters idle faze in which all the entities may send offers,
     *  - enters active faze at which markets process and update offers and managers process end day events.
     */
    public void runSimulationDay() {
        this.day++;
        GlobalHoldersLock.writeUnlock();
        try {
            Thread.sleep((long) (Constants.BASE_TRADING_TIME * SimulationConfig.getInstance().getTimeMultiplier()));
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

    /**
     * Unlocks the modification of the entities and stops them. (Thus ending the simulation).
     */
    public void stop() {
        GlobalHoldersLock.writeUnlock();
        this.tradingEntitiesManager.stopEntities();
    }

    /**
     * Resets investor strategies and starts all entities, but blocks them from participating in trade outside
     * the runSimulationDay method.
     */
    public void start() {
        this.tradingEntitiesManager.setInvestorStrategies();
        this.tradingEntitiesManager.startEntities();
        this.started = true;
        GlobalHoldersLock.writeLock();
    }

    /**
     * Adds new random market of the given category to the pool of markets, filling it with new random assets.
     * @param category category of the market to be created.
     */
    public void setupRandomMarket(AssetCategory category) {
        this.addNewMarket(new RandomMarketFactory(this.assetManager, this.tradingEntitiesManager).createMarket(category));
    }

    /**
     * Adds a market to the pool of markets and adjusts the number of investors.
     * @param market
     */
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

    /**
     * Returns a market with the provided name. Throws exception if non-existent market name is passed.
     * @param name name of the market to be fetched.
     * @return market with the provided name.
     */
    public Market getMarketByName(String name) {
        for (var market : this.markets) {
            if (market.getName().equals(name)) {
                return market;
            }
        }
        throw new IllegalArgumentException("Invalid market name: " + name);
    }

    /**
     * Checks if there is a market with name provided as a parameter.
     * @param name queried market name.
     * @return boolean denoting whether there exists market with a provided name or not.
     */
    public boolean doesMarketExist(String name) {
        for (var market : this.markets) {
            if (market.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasStarted() {
        return this.started;
    }

    public int getSimulationDay() {
        return this.day;
    }
}
