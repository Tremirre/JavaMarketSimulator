package simulation.holders;


import simulation.asset.AssetManager;
import simulation.market.Market;
import simulation.market.StockMarketIndex;

import java.util.ArrayList;
import java.util.HashSet;

public final class TradingEntitiesManager {
    private final ArrayList<AssetHolder> entities = new ArrayList<>();
    private final HashSet<StockMarketIndex> stockMarketIndexes = new HashSet<>();
    private final HashSet<Market> availableMarkets;
    private final AssetManager assetManager;


    public TradingEntitiesManager(AssetManager assetManager, HashSet<Market> markets) {
        this.assetManager = assetManager;
        this.availableMarkets = markets;
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

    public Company createNewCompany() {
        var newCompany = new RandomHolderFactory(this.assetManager).createCompany();
        newCompany.giveAccessToMarkets(this.availableMarkets);
        this.entities.add(newCompany);
        return newCompany;
    }

    public InvestmentFund createNewFund() {
        var newFund = new RandomHolderFactory(this.assetManager).createInvestmentFund();
        newFund.giveAccessToMarkets(this.availableMarkets);
        this.entities.add(newFund);
        return newFund;
    }

    public Investor createNewInvestor() {
        var newInvestor = new RandomHolderFactory(this.assetManager).createInvestor();
        newInvestor.giveAccessToMarkets(this.availableMarkets);
        this.entities.add(newInvestor);
        return newInvestor;
    }

    public Company createCompanyAndRun() {
        var newCompany = this.createNewCompany();
        newCompany.start();
        return newCompany;
    }

    public InvestmentFund createInvestmentFundAndRun() {
        var newFund = this.createNewFund();
        newFund.start();
        return newFund;
    }

    public Investor createInvestorAndRun() {
        var newInvestor = this.createNewInvestor();
        newInvestor.start();
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

    public void autoCreateInvestors(boolean run) {
        int neededInvestors = this.assetManager.getNumberOfAssetTypes() * 10;
        for (var entity : entities)
            neededInvestors -= entity instanceof Investor ? 1 : 0;
        for (int i = 0; i < neededInvestors; i++) {
            var investor = run ? this.createInvestorAndRun() : this.createNewInvestor();
        }
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
}
