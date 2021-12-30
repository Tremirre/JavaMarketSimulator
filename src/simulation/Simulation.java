package simulation;

import simulation.asset.AssetManager;
import simulation.holders.Company;
import simulation.holders.Investor;
import simulation.holders.RandomHolderFactory;
import simulation.market.*;
import simulation.util.Constants;
import simulation.util.GlobalMarketLock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class Simulation {
    private final ArrayList<Market> markets;
    private HashSet<Investor> investors;
    public ArrayList<Company> companies;

    public Simulation() {
        this.markets = new ArrayList<>();
        this.setupStockMarket();
        this.setupCurrenciesMarket();
        this.setupCommoditiesMarket();
        this.setupInvestors();
        this.start();
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

    public void stop() {
        for (var investor : this.investors) {
            investor.stopRunning();
        }
    }

    private void start() {
        for (var investor : this.investors) {
            investor.start();
        }
    }

    private void setupStockMarket() {
        var market = new StockMarket("StockTest" + this.markets.size(), 0.01, 0.02, "US Dollar");
        this.markets.add(market);
    }

    private void setupCurrenciesMarket() {
        var market = new CurrenciesMarket("CurrencyTest", 0.01, 0.02);
        this.markets.add(market);
    }

    private void setupCommoditiesMarket() {
        var market = new CommoditiesMarket("CommodityTest", 0.01, 0.2);
        this.markets.add(market);
    }

    public void pause() {
        GlobalMarketLock.writeLock();
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        GlobalMarketLock.writeUnlock();
    }

    private void setupInvestors() {
        this.investors = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            var newInvestor = new RandomHolderFactory().createInvestor();
            newInvestor.giveAccessToMarkets(new HashSet<>(this.markets));
            this.investors.add(newInvestor);
        }
    }
}
