package simulation;

import simulation.asset.AssetManager;
import simulation.holders.Company;
import simulation.holders.Investor;
import simulation.holders.RandomHolderFactory;
import simulation.market.Market;
import simulation.market.StockMarket;
import simulation.market.StockMarketIndex;
import simulation.util.Constants;

import java.util.ArrayList;
import java.util.HashSet;

public class Simulation {
    private HashSet<Market> markets;
    private HashSet<Investor> investors;
    public ArrayList<Company> companies;
    private SimulationConfig simConfig;

    public Simulation() {
        this.simConfig = new SimulationConfig();
        this.markets = new HashSet<>();
        var market = new StockMarket("Test", 0.01, 0.02, "USD");
        this.companies = new ArrayList<>();
        var stockIndex = new StockMarketIndex();

        for (int i = 0; i < 10; i++) {
            this.companies.add(new RandomHolderFactory().createCompany());
            stockIndex.addCompany(this.companies.get(this.companies.size() - 1));
        }

        market.addStockMarketIndex(stockIndex);
        for (var company : companies) {
            company.sendSellOffer(market);
        }
        this.markets.add(market);
        this.investors = new HashSet<>();
        for (int i = 0; i < 200; i++) {
            var newInvestor = new RandomHolderFactory().createInvestor();
            newInvestor.giveAccessToMarkets(this.markets);
            this.investors.add(newInvestor);
        }
        for (var investor : this.investors) {
            investor.start();
        }
    }

    public void runSimulationDay() {
        try {
            Thread.sleep(Constants.BASE_TRADING_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (var market : this.markets) {
            market.processAllOffers();
            market.updateOffers();
            market.removeOutdatedOffers();
            AssetManager.getInstance().processEndDay();
        }
    }

    public void stopSimulation() {
        for (var investor : this.investors) {
            investor.stopRunning();
        }
    }
}
