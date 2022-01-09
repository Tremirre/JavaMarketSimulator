package main.java.simulation.holders;

import main.java.simulation.holders.strategies.InvestmentStrategy;
import main.java.simulation.holders.strategies.PassiveCompanyStrategy;
import main.java.simulation.market.Market;
import main.java.simulation.util.Constants;
import main.java.simulation.util.GlobalHoldersLock;
import main.java.simulation.util.RandomService;

public class InvestmentFund extends Company {
    private String ownerName;
    private String ownerSurname;
    private double currentCycleProfit;
    private double currentCycleRevenue;
    private static final int pulloutCycle = 10;

    public InvestmentFund(int id,
                          int numberOfStocks,
                          String IPODate,
                          Address address,
                          double IPOShareValue,
                          InvestmentStrategy strategy,
                          double investmentBudget,
                          String ownerName,
                          String ownerSurname) {
        super(id, numberOfStocks, ownerName + ' ' + ownerSurname + "'s Investment Fund",
                IPODate, address, IPOShareValue, 0, 0, strategy);
        this.ownerName = ownerName;
        this.ownerSurname = ownerSurname;
        this.setInvestmentBudget(investmentBudget);
        this.currentCycleProfit = 0;
        this.currentCycleRevenue = 0;
        this.freezeWithdrawal = false;
    }

    private void pulloutFunds() {
        this.investmentBudget *= 0.99;
    }

    @Override
    protected void updateCompanyData() {
        this.profit = this.currentCycleProfit;
        this.revenue = this.currentCycleRevenue;
        this.currentCycleRevenue = 0;
        this.currentCycleProfit = 0;
    }

    @Override
    public void sellAllStocks(Market market) {
        var savedStrategy = this.strategy;
        this.strategy = new PassiveCompanyStrategy(this.strategy.getAssetManager());
        this.sendSellOffer(market);
        this.strategy = savedStrategy;
    }

    @Override
    public void sendBuyOffer(Market market) {
        var diff = -this.investmentBudget;
        super.sendBuyOffer(market);
        diff += this.investmentBudget;
        this.currentCycleProfit -= diff;
    }

    @Override
    public void processBuyOffer(String assetType, double overPay, double amount) {
        var diff = -this.investmentBudget;
        super.processBuyOffer(assetType, overPay, amount);
        diff += this.investmentBudget;
        this.currentCycleProfit += diff;
        this.currentCycleRevenue += diff;
    }

    @Override
    public void processBuyWithdrawal(double price, double amount, String currency) {
        var diff = -this.investmentBudget;
        super.processBuyWithdrawal(price, amount, currency);
        diff += this.investmentBudget;
        this.currentCycleProfit += diff;
        this.currentCycleRevenue += diff;
    }

    @Override
    public double processBuyOfferAlteration(double price, double amount, String assetType, String currency) {
        var diff = -this.investmentBudget;
        var newPrice = super.processBuyOfferAlteration(price, amount, assetType, currency);
        diff += this.investmentBudget;
        this.currentCycleProfit -= diff;
        return newPrice;
    }

    @Override
    public void processSellOffer(String assetType, double price, double amount) {
        var diff = -this.investmentBudget;
        super.processSellOffer(assetType, price, amount);
        diff += this.investmentBudget;
        this.currentCycleProfit += diff;
        this.currentCycleRevenue += diff;
    }

    @Override
    public double processSellOfferAlteration(double price, String assetType, String assetCurrency) {
        if (this.associatedAsset.equals(assetType))
            return price;
        return super.processSellOfferAlteration(price, assetType, assetCurrency);
    }

    @Override
    public boolean canWithdraw(String assetType) {
        if (this.associatedAsset.equals(assetType))
            return false;
        return !this.freezeWithdrawal;
    }

    @Override
    public void run() {
        int cycleNumber = 0;
        while (this.running) {
            GlobalHoldersLock.readLock();
            this.generateOrders();
            if (cycleNumber++ == pulloutCycle) {
                this.pulloutFunds();
                this.updateCompanyData();
            }
            GlobalHoldersLock.readUnlock();
            try {
                Thread.sleep(RandomService.getInstance().yieldRandomInteger(
                        Constants.INVESTMENT_FUND_MAXIMAL_ADDITIONAL_SLEEP_TIME) +
                        Constants.BASE_INVESTMENT_FUND_SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
