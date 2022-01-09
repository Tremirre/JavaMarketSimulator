package main.java.simulation.holders;

import main.java.simulation.holders.strategies.InvestmentStrategy;
import main.java.simulation.market.Market;
import main.java.simulation.util.Constants;
import main.java.simulation.util.GlobalHoldersLock;
import main.java.simulation.util.RandomService;

import java.util.ArrayList;

public class Company extends AssetHolder {
    private String name;
    private final String IPODate;
    private Address address;
    protected double profit;
    protected double revenue;
    protected final String associatedAsset;
    protected final ArrayList<Integer> dailyTradingVolumes = new ArrayList<>();
    protected final ArrayList<Double> dailyTotalSales = new ArrayList<>();
    protected int currentTradingVolume = 0; //number of transactions of stocks of that company
    protected double currentTotalSales = 0;
    protected int numberOfStocks;

    public Company(int id,
                   int numberOfStocks,
                   String name,
                   String IPODate,
                   Address address,
                   double IPOShareValue,
                   double profit,
                   double revenue,
                   InvestmentStrategy strategy) {
        super(id, 0, strategy);
        this.name = name;
        this.IPODate = IPODate;
        this.address = address;
        this.profit = profit;
        this.revenue = revenue;
        this.numberOfStocks = numberOfStocks;
        StringBuilder stockName = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            if (Character.isLetter(name.charAt(i))) stockName.append(name.charAt(i));
        }
        var stock = this.strategy.getAssetManager().addStockAsset(stockName.toString().toUpperCase(), IPOShareValue, this);
        this.associatedAsset = stock.getUniqueIdentifyingName();
        this.storedAssets.put(this.associatedAsset, (double) numberOfStocks);
        this.freezeWithdrawal = true;
    }

    public void buyout() {
        for (var market : this.availableMarkets) {
            if (market.getAvailableAssetTypes().contains(this.associatedAsset)) {
                var price = this.strategy.determineOptimalBuyingPrice(this.associatedAsset);
                market.addSellOffer(this.associatedAsset, this, price, this.numberOfStocks);
                return;
            }
        }
    }

    public void sellAllStocks(Market market) {
        this.sendSellOffer(market);
    }

    public synchronized void processSellOffer(String assetType, double price, double amount) {
        this.revenue += price * amount;
    }

    public String getCompanyName() {
        return name;
    }

    public void setCompanyName(String name) {
        this.name = name;
    }

    public String getIPODate() {
        return IPODate;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public double getCapital() {
        return this.numberOfStocks * this.strategy.getAssetManager()
                .getAssetData(this.associatedAsset)
                .getOpeningPrice();
    }

    public void recordTransactionData(double price, double amount) {
        this.currentTradingVolume += amount;
        this.currentTotalSales += price * amount;
    }

    public void saveAndResetStockTransactionsData() {
        this.dailyTotalSales.add(this.currentTotalSales);
        this.dailyTradingVolumes.add(this.currentTradingVolume);
        this.currentTotalSales = 0;
        this.currentTradingVolume = 0;
    }

    public void endDayEvent() { this.saveAndResetStockTransactionsData(); }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    protected void updateCompanyData() {
        var rand = RandomService.getInstance();
        this.revenue += rand.yieldRandomGaussianNumber(1000, 0);
        this.profit += rand.yieldRandomGaussianNumber(500, 0);
        this.profit = Math.min(this.revenue, this.profit);
    }

    protected void increaseNumberOfShares() {
        int newStocks = this.numberOfStocks/2;
        this.numberOfStocks += newStocks;
        this.storedAssets.put(this.associatedAsset, (double) newStocks);
        for (var market : this.availableMarkets) {
            if (market.getAvailableAssetTypes().contains(this.associatedAsset)) {
                this.sellAllStocks(market);
                return;
            }
        }
    }

    @Override
    public void run() {
        while(this.running) {
            try {
                Thread.sleep(Constants.COMPANY_SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            GlobalHoldersLock.readLock();
            this.updateCompanyData();
            if (RandomService.getInstance().yieldRandomNumber(1) < Constants.COMPANY_SHARES_INCREASE_PROBABILITY)
                this.increaseNumberOfShares();
            GlobalHoldersLock.readUnlock();
        }
    }

    public String getAssociatedAsset() {
        return associatedAsset;
    }
}
