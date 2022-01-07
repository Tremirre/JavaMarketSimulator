package simulation;

import simulation.asset.AssetManager;
import simulation.asset.RandomSupplementaryAssetFactory;
import simulation.holders.CompaniesManager;
import simulation.holders.Investor;
import simulation.holders.RandomHolderFactory;
import simulation.market.*;
import simulation.util.Constants;
import simulation.util.GlobalHoldersLock;
import simulation.util.ResourceHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class Simulation {
    private final ArrayList<Market> markets;
    private HashSet<Investor> investors;

    public Simulation() {
        this.markets = new ArrayList<>();
        this.setupDataResourceForRandomFactories();
        this.setupMarket(MarketType.STOCK_MARKET);
        var market = (StockMarket) this.markets.get(0);
        var fundsIdx = new StockMarketIndex();
        for (int i = 0; i < 10; i++) {
            fundsIdx.addCompany(CompaniesManager.getInstance().createNewFund());
        }
        market.addStockMarketIndex(fundsIdx);
        this.setupMarket(MarketType.COMMODITIES_MARKET);
        this.setupMarket(MarketType.CURRENCIES_MARKET);
        this.setupInvestors();
        CompaniesManager.getInstance().giveAccessToMarkets(new HashSet<>(this.markets));
        this.start();

    }

    public void runSimulationDay(int day) {
        try {
            Thread.sleep(Constants.BASE_TRADING_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        GlobalHoldersLock.writeLock();
        for (var market : this.markets) {
            market.processAllOffers();
            market.updateOffers();
            market.removeOutdatedOffers();
        }
        /*if (day == 1) {
            SimulationConfig.getInstance().setBullProportion(0.65);
        } else if (day == Constants.YEAR) {
            SimulationConfig.getInstance().setBullProportion(0.35);
        } else if (day == Constants.YEAR + 100) {
            SimulationConfig.getInstance().setBullProportion(0.65);
        }*/
        AssetManager.getInstance().processEndDay();
        CompaniesManager.getInstance().processEndDay();
        double total = 0;
        for (var investor : this.investors) {
            total += investor.getInvestmentBudget();
        }
        System.out.println("Total investor's funds: " + total);
        GlobalHoldersLock.writeUnlock();
    }

    public void stop() {
        for (var investor : this.investors) {
            investor.stopRunning();
        }
        CompaniesManager.getInstance().stopCompanies();
    }

    private void start() {
        for (var investor : this.investors) {
            investor.start();
        }
    }

    private void setupMarket(MarketType type) {
        this.markets.add(new RandomMarketFactory().createMarket(type));
    }

    public void pause() {
        GlobalHoldersLock.writeLock();
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        GlobalHoldersLock.writeUnlock();
    }

    private void setupInvestors() {
        this.investors = new HashSet<>();
        for (int i = 0; i < 400; i++) {
            var newInvestor = new RandomHolderFactory().createInvestor();
            newInvestor.giveAccessToMarkets(new HashSet<>(this.markets));
            this.investors.add(newInvestor);
        }
    }

    private void setupDataResourceForRandomFactories() {
        var resourceHolder = new ResourceHolder();
        RandomHolderFactory.setFactoryResource(resourceHolder);
        RandomSupplementaryAssetFactory.setFactoryResource(resourceHolder);
        RandomMarketFactory.setFactoryResource(resourceHolder);
    }
}
