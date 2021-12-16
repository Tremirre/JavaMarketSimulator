package simulation;

import simulation.holders.Company;
import simulation.holders.HolderType;
import simulation.holders.Investor;
import simulation.holders.RandomHolderFactory;
import simulation.market.Market;
import simulation.market.StockMarket;
import simulation.market.StockMarketIndex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class Simulation {
    private ArrayList<Market> markets;
    private ArrayList<Investor> investors;
    public ArrayList<Company> companies;
    private SimulationConfig simConfig;

    public Simulation() {
        this.simConfig = new SimulationConfig();
        this.markets = new ArrayList<>();
        this.markets.add(new StockMarket("Test", 0.01, 0.02, "USD"));
        this.companies = new ArrayList<>();
        this.companies.add((Company) new RandomHolderFactory().createHolder(HolderType.COMPANY));
        var stockIndex = new StockMarketIndex();
        stockIndex.addCompany(companies.get(0));
        this.investors = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            investors.add((Investor) new RandomHolderFactory().createHolder(HolderType.INVESTOR));
            this.investors.get(this.investors.size() - 1).giveAccessToMarkets(new HashSet<>(this.markets));
        }
        ((StockMarket) this.markets.get(0)).addStockMarketIndex(stockIndex);
        this.companies.get(0).sendSellOffer(this.markets.get(0));
        for (var investor : investors) {
            investor.start();
        }
    }

    public void runSimulationDay() throws InterruptedException {
        Thread.sleep(100);
        this.markets.get(0).processAllOffers();
        this.markets.get(0).updateOffers();
    }

    public void stopSimulation() {
        for (var investor : this.investors) {
            investor.stopRunning();
        }
    }
}
