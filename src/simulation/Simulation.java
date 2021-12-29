package simulation;

import simulation.asset.AssetManager;
import simulation.holders.Company;
import simulation.holders.Investor;
import simulation.holders.RandomHolderFactory;
import simulation.market.*;
import simulation.util.Constants;
import simulation.util.GlobalMarketLock;

import java.util.ArrayList;
import java.util.HashSet;

public class Simulation {
    private ArrayList<Market> markets;
    private HashSet<Investor> investors;
    public ArrayList<Company> companies;

    public Simulation() {
        this.markets = new ArrayList<>();
        this.companies = new ArrayList<>();
        this.setupStockMarket();
        this.setupCurrenciesMarket();
        this.setupCommoditiesMarket();
        this.setupInvestors();
    }

    public void runSimulationDay() {
        try {
            Thread.sleep(Constants.BASE_TRADING_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        GlobalMarketLock.writeLock();
        for (var market : this.markets) {
            market.processAllOffers();
            market.updateOffers();
            market.removeOutdatedOffers();
        }
        AssetManager.getInstance().processEndDay();
        GlobalMarketLock.writeUnlock();
    }

    public void stopSimulation() {
        for (var investor : this.investors) {
            investor.stopRunning();
        }
    }

    private void setupStockMarket() {
        var market = new StockMarket("Test" + this.markets.size(), 0.01, 0.02, "US Dollar");
        var stockIndex = new StockMarketIndex();

        for (int i = 0; i < 3; i++) {
            this.companies.add(new RandomHolderFactory().createCompany());
            stockIndex.addCompany(this.companies.get(this.companies.size() - 1));
        }
        market.addStockMarketIndex(stockIndex);
        this.markets.add(market);
    }

    private void setupCurrenciesMarket() {
        var market = new CurrenciesMarket("Test", 0.01, 0.02);
        this.markets.add(market);
    }

    private void setupCommoditiesMarket() {
        var market = new CommoditiesMarket("Test", 0.01, 0.2);
        this.markets.add(market);
    }

    private void setupInvestors() {
        this.investors = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            var newInvestor = new RandomHolderFactory().createInvestor();
            newInvestor.giveAccessToMarkets(new HashSet<>(this.markets));
            this.investors.add(newInvestor);
        }
        for (var investor : this.investors) {
            investor.start();
        }
    }
}
