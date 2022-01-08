package simulation.holders;

import simulation.asset.AssetManager;
import simulation.market.Market;

import java.util.ArrayList;
import java.util.HashSet;

public final class TradingEntitiesManager {
    private final ArrayList<AssetHolder> entities = new ArrayList<>();
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
        throw new IllegalStateException("Invalid entity ID passed to the manager!: " + id);
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
}