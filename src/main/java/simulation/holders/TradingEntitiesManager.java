package simulation.holders;


import simulation.address.RandomAddressFactory;
import simulation.asset.AssetCategory;
import simulation.asset.AssetManager;
import simulation.core.SimulationConfig;
import simulation.holders.strategies.MomentumInvestmentStrategy;
import simulation.holders.strategies.NaiveInvestmentStrategy;
import simulation.holders.strategies.QualitativeAssessmentStrategy;
import simulation.market.InformedMarketFactory;
import simulation.market.Market;
import simulation.market.StockMarket;
import simulation.market.StockMarketIndex;
import simulation.util.Constants;
import simulation.util.RandomService;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Class tasked with management and storing of all trading entities in the simulation.
 */
public final class TradingEntitiesManager {
    /**
     * List of stored entities.
     */
    private final ArrayList<AssetHolder> entities = new ArrayList<>();
    /**
     * Set of stored stock market indexes.
     */
    private final HashSet<StockMarketIndex> stockMarketIndexes = new HashSet<>();
    /**
     * Set of available markets.
     */
    private final HashSet<Market> availableMarkets;
    /**
     * Reference to the asset manager.
     */
    private final AssetManager assetManager;
    /**
     * Special stock market index containing all investment funds.
     */
    private final StockMarketIndex fundex;
    /**
     * Special stock market on which only investment funds' shares may be traded.
     */
    private final StockMarket fundMarket;

    /**
     * Initializes new Trading Entities Manager with reference to the asset manager and the set of available markets.
     * Creates index and market for investment funds and adds it to the pool of all markets.
     * @param assetManager reference to the asset manager.
     * @param markets reference to the set of available markets.
     */
    public TradingEntitiesManager(AssetManager assetManager, HashSet<Market> markets) {
        this.assetManager = assetManager;
        this.availableMarkets = markets;
        this.fundex = this.createNewStockIndex("FUNDEX");
        this.fundMarket = (StockMarket) new InformedMarketFactory(this.assetManager,
                "FUNDEX",
                new RandomAddressFactory().createAddress(),
                0.05,
                0.05
                ).createMarket(AssetCategory.STOCK);
        this.fundMarket.setMarketCurrency(Constants.DEFAULT_CURRENCY);
        this.fundMarket.addStockMarketIndex(this.fundex);
        this.availableMarkets.add(fundMarket);
    }

    /**
     * Returns asset holder with the provided id. Throws exception if non-existent id is passed to the manager.
     * @param id id of the asset holder to be found.
     * @return found entity.
     */
    public AssetHolder getHolderByID(int id) {
        for (var entity : entities) {
            if (entity.getID() == id) {
                return entity;
            }
        }
        throw new IllegalArgumentException("Invalid entity ID passed to the manager!: " + id);
    }

    /**
     * Processes end day events of all stored entities.
     */
    public void processEndDay() {
        for (var entity : entities)
            entity.endDayEvent();
    }

    /**
     * Creates and stores new company.
     * In case the factory failed to create a company, it passes null further.
     * @param factory factory to be used for creating new company.
     * @return new Company with passed reference to available markets.
     */
    public Company createNewCompany(HolderFactory factory) {
        var newCompany = factory.createCompany();
        if (newCompany == null) {
            return null;
        }
        newCompany.giveAccessToMarkets(this.availableMarkets);
        this.entities.add(newCompany);
        return newCompany;
    }

    /**
     * Creates and stores new investment fund.
     * In case the factory failed to create an investment fund, it passes null further.
     * New fund is added to the index for Investment Funds.
     * @param factory factory to be used for creating new investment fund.
     * @return new investment fund with passed reference to available markets.
     */
    public InvestmentFund createNewFund(HolderFactory factory) {
        var newFund = factory.createInvestmentFund();
        if (newFund == null) {
            return null;
        }
        newFund.giveAccessToMarkets(this.availableMarkets);
        this.entities.add(newFund);
        this.fundex.addCompany(newFund);
        return newFund;
    }

    /**
     * Creates and stores new investor.
     * In case the factory failed to create an investor, it passes null further.
     * @param factory factory to be used for creating new investor.
     * @return new Investor with passed reference to available markets.
     */
    public Investor createNewInvestor(HolderFactory factory) {
        var newInvestor = factory.createInvestor();
        if (newInvestor == null) {
            return null;
        }
        newInvestor.giveAccessToMarkets(this.availableMarkets);
        this.entities.add(newInvestor);
        return newInvestor;
    }

    /**
     * Stops all entities.
     */
    public void stopEntities() {
        for (var entity : entities)
            entity.stopRunning();
    }

    /**
     * Starts all entities.
     */
    public void startEntities() {
        for (var entity : entities)
            entity.start();
    }

    /**
     * Auto creates new investors and investment funds based on the number of assets in the simulation.
     */
    public void autoCreateInvestors() {
        int neededInvestors = this.assetManager.getNumberOfAssetTypes() * 16;
        int neededFunds = neededInvestors / 64;
        for (var entity : entities) {
            neededInvestors -= entity instanceof Investor ? 1 : 0;
            neededFunds -= entity instanceof InvestmentFund ? 1 : 0;
        }
        for (int i = 0; i < neededInvestors; i++) {
            this.createNewInvestor(new RandomHolderFactory(this.getTotalNumberOfEntities(), this.assetManager));
        }
        for (int i = 0; i < neededFunds; i++) {
            var fund = this.createNewFund(new RandomHolderFactory(this.getTotalNumberOfEntities(),  this.assetManager));
            if (fund == null) {
                continue;
            }
        }
        this.fundMarket.refreshAssets();
    }

    /**
     * Creates and stores new stock market index.
     * @param name name of the new index.
     * @return new Stock Market Index.
     */
    public StockMarketIndex createNewStockIndex(String name) {
        var newIdx = new StockMarketIndex(name);
        this.stockMarketIndexes.add(newIdx);
        return newIdx;
    }

    /**
     * Checks if the given stock market index name is free for use.
     * @param name queried name of the index.
     * @return flag denoting whether the given name can be used or not.
     */
    public boolean isIndexNameFree(String name) {
        for (var idx : this.stockMarketIndexes) {
            if (idx.getName().equals(name))
                return false;
        }
        return true;
    }

    /**
     * Returns stock market index by name. Throws exception if non-existent name is passed.
     * @param name queried name of the index.
     * @return index with the given name.
     */
    public StockMarketIndex getIndexByName(String name) {
        for (var idx : this.stockMarketIndexes) {
            if (idx.getName().equals(name))
                return idx;
        }
        throw new IllegalArgumentException("Invalid Stock Market Index name passed to TEM: " + name);
    }

    public HashSet<StockMarketIndex> getStockMarketIndexes() {
        return this.stockMarketIndexes;
    }

    /**
     * Aggregates all investors from the pool of stored entities.
     * @return set of investors in the simulation.
     */
    public HashSet<Investor> getInvestors() {
        HashSet<Investor> investors = new HashSet<>();
        for (var entity : entities) {
            if (entity instanceof Investor)
                investors.add((Investor) entity);
        }
        return investors;
    }

    /**
     * Counts the number of entities in the simulation.
     * @return number of entities.
     */
    public int getTotalNumberOfEntities() {
        return this.entities.size();
    }

    /**
     * Runs all entities that haven't been started yet.
     */
    public void runIdleEntities() {
        for (var entity : entities) {
            if (!entity.isRunning())
                entity.start();
        }
    }

    /**
     * Resets investors' investment strategies according to the proportions set in the SimulationConfig.
     * @see SimulationConfig
     */
    public void setInvestorStrategies() {
        var rand = RandomService.getInstance();
        var investors = new ArrayList<>(this.getInvestors());
        var naiveProportion = SimulationConfig.getInstance().getNaiveProportion();
        var qualitativeProportion = SimulationConfig.getInstance().getQualitativeProportion();
        int naiveInvestors = (int) (investors.size() * naiveProportion);
        int qualitativeInvestors = (int) (investors.size() * qualitativeProportion);
        int i = 0;
        while(i < naiveInvestors) {
            investors.get(i++).setStrategy(
                    new NaiveInvestmentStrategy(
                        this.assetManager,
                        rand.yieldRandomGaussianNumber(0.03, 0.9)
                    )
            );
        }
        while(i < naiveInvestors + qualitativeInvestors) {
            investors.get(i++).setStrategy(
                    new QualitativeAssessmentStrategy(
                            this.assetManager,
                            rand.yieldRandomGaussianNumber(0.05, 1)
                    )
            );
        }
        while (i < investors.size()) {
            investors.get(i++).setStrategy(
                    new MomentumInvestmentStrategy(
                            this.assetManager,
                            rand.yieldRandomInteger(5) + 1
                    )
            );
        }

    }
}
