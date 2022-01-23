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

public final class TradingEntitiesManager {
    private final ArrayList<AssetHolder> entities = new ArrayList<>();
    private final HashSet<StockMarketIndex> stockMarketIndexes = new HashSet<>();
    private final HashSet<Market> availableMarkets;
    private final AssetManager assetManager;
    private final StockMarketIndex fundex;
    private final StockMarket fundMarket;

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

    public AssetHolder getHolderByID(int id) {
        for (var entity : entities) {
            if (entity.getID() == id) {
                return entity;
            }
        }
        throw new IllegalArgumentException("Invalid entity ID passed to the manager!: " + id);
    }

    public void processEndDay() {
        for (var entity : entities)
            entity.endDayEvent();
    }

    public Company createNewCompany(HolderFactory factory) {
        var newCompany = factory.createCompany();
        if (newCompany == null) {
            return null;
        }
        newCompany.giveAccessToMarkets(this.availableMarkets);
        this.entities.add(newCompany);
        return newCompany;
    }

    public InvestmentFund createNewFund(HolderFactory factory) {
        var newFund = factory.createInvestmentFund();
        if (newFund == null) {
            return null;
        }
        newFund.giveAccessToMarkets(this.availableMarkets);
        this.entities.add(newFund);
        return newFund;
    }

    public Investor createNewInvestor(HolderFactory factory) {
        var newInvestor = factory.createInvestor();
        if (newInvestor == null) {
            return null;
        }
        newInvestor.giveAccessToMarkets(this.availableMarkets);
        this.entities.add(newInvestor);
        return newInvestor;
    }

    public void stopEntities() {
        for (var entity : entities)
            entity.stopRunning();
    }

    public void startEntities() {
        for (var entity : entities)
            entity.start();
    }

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
            this.fundex.addCompany(fund);
        }
        this.fundMarket.refreshAssets();
    }

    public StockMarketIndex createNewStockIndex(String name) {
        var newIdx = new StockMarketIndex(name);
        this.stockMarketIndexes.add(newIdx);
        return newIdx;
    }

    public void addNewIndex(StockMarketIndex idx) {
        this.stockMarketIndexes.add(idx);
    }

    public boolean isIndexNameFree(String name) {
        for (var idx : this.stockMarketIndexes) {
            if (idx.getName().equals(name))
                return false;
        }
        return true;
    }

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

    public HashSet<Investor> getInvestors() {
        HashSet<Investor> investors = new HashSet<>();
        for (var entity : entities) {
            if (entity instanceof Investor)
                investors.add((Investor) entity);
        }
        return investors;
    }

    public int getTotalNumberOfEntities() {
        return this.entities.size();
    }

    public void runIdleEntities() {
        for (var entity : entities) {
            if (!entity.isRunning())
                entity.start();
        }
    }

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
