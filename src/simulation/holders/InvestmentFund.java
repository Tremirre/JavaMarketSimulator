package simulation.holders;

import simulation.holders.strategies.InvestmentStrategy;
import simulation.holders.strategies.PassiveCompanyStrategy;
import simulation.market.Market;
import simulation.market.StockMarket;
import simulation.util.Constants;
import simulation.util.GlobalHoldersLock;
import simulation.util.RandomService;

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
                IPODate, address, IPOShareValue, 0, 0);
        this.ownerName = ownerName;
        this.ownerSurname = ownerSurname;
        this.strategy = strategy;
        this.setInvestmentBudget(investmentBudget);
        this.currentCycleProfit = 0;
        this.currentCycleRevenue = 0;
        this.freezeWithdrawal = false;
    }

    private void pulloutFunds() {
        this.investmentBudget *= 0.99;
    }

    protected void updateCompanyData() {
        this.profit = this.currentCycleProfit;
        this.revenue = this.currentCycleRevenue;
        this.currentCycleRevenue = 0;
        this.currentCycleProfit = 0;
    }

    public void sendInitialOffer(StockMarket stockMarket) {
        var savedStrategy = this.strategy;
        this.strategy = new PassiveCompanyStrategy();
        this.sendSellOffer(stockMarket);
        this.strategy = savedStrategy;
    }
    
    public void sendBuyOffer(Market market) {
        var diff = -this.investmentBudget;
        super.sendBuyOffer(market);
        diff += this.investmentBudget;
        this.currentCycleProfit -= diff;
    }

    public void processBuyOffer(String assetType, double overPay, double amount) {
        var diff = -this.investmentBudget;
        super.processBuyOffer(assetType, overPay, amount);
        diff += this.investmentBudget;
        this.currentCycleProfit += diff;
        this.currentCycleRevenue += diff;
    }

    public void processBuyWithdrawal(double price, double amount) {
        var diff = -this.investmentBudget;
        super.processBuyWithdrawal(price, amount);
        diff += this.investmentBudget;
        this.currentCycleProfit += diff;
        this.currentCycleRevenue += diff;
    }

    public double processBuyOfferAlteration(double price, double amount, String assetType) {
        var diff = -this.investmentBudget;
        var newPrice = super.processBuyOfferAlteration(price, amount, assetType);
        diff += this.investmentBudget;
        this.currentCycleProfit -= diff;
        return newPrice;
    }

    public void processSellOffer(String assetType, double price, double amount) {
        var diff = -this.investmentBudget;
        super.processSellOffer(assetType, price, amount);
        diff += this.investmentBudget;
        this.currentCycleProfit += diff;
        this.currentCycleRevenue += diff;
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
