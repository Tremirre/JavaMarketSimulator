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

public class Simulation {
    private ArrayList<Market> markets;
    private ArrayList<Investor> investors;
    private ArrayList<Company> companies;
    private SimulationConfig simConfig;

    public Simulation() throws IOException {
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
        }
    }

    public void runSimulationDay() {

    }
}
