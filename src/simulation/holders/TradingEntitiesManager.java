package simulation.holders;

import simulation.market.Market;

import java.util.ArrayList;
import java.util.HashSet;

public final class TradingEntitiesManager {
    private ArrayList<AssetHolder> entities = new ArrayList<>();
    private HashSet<Market> availableMarkets;

    public TradingEntitiesManager(HashSet<Market> markets) {
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
        var newCompany = new RandomHolderFactory().createCompany();
        newCompany.giveAccessToMarkets(this.availableMarkets);
        this.entities.add(newCompany);
        return newCompany;
    }

    public InvestmentFund createNewFund() {
        var newFund = new RandomHolderFactory().createInvestmentFund();
        newFund.giveAccessToMarkets(this.availableMarkets);
        this.entities.add(newFund);
        return newFund;
    }

    public Investor createNewInvestor() {
        var newInvestor = new RandomHolderFactory().createInvestor();
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
