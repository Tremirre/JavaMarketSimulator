package simulation.holders;

import simulation.asset.AssetManager;
import simulation.holders.strategies.PassiveCompanyStrategy;
import simulation.market.StockMarket;
import simulation.util.Constants;
import simulation.util.GlobalHoldersLock;
import simulation.util.RandomService;

import java.util.ArrayList;

public class Company extends AssetHolder {
    private String name;
    private final String IPODate;
    private Address address;
    private double profit;
    private double revenue;
    private final String associatedAsset;
    private final ArrayList<Integer> dailyTradingVolumes = new ArrayList<>();
    private final ArrayList<Double> dailyTotalSales = new ArrayList<>();
    private int currentTradingVolume = 0; //number of transactions of stocks of that company
    private double currentTotalSales = 0;
    private int numberOfStocks;

    public Company(int id,
                   int numberOfStocks,
                   String name,
                   String IPODate,
                   Address address,
                   double IPOShareValue,
                   double profit,
                   double revenue) {
        super(id, 0, new PassiveCompanyStrategy());
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
        var stock = AssetManager.getInstance().addStockAsset(stockName.toString().toUpperCase(), IPOShareValue, this);
        this.associatedAsset = stock.getUniqueIdentifyingName();
        this.storedAssets.put(this.associatedAsset, (double) numberOfStocks);
        this.freezeWithdrawal = true;
    }

    public void buyout(StockMarket stockMarket) {

    }

    public void sendInitialOffer(StockMarket stockMarket) {
        this.sendSellOffer(stockMarket);
    }

    public void print() {
        System.out.println(this.name);
        this.address.print();
        System.out.println("IPO date: " + this.IPODate);
        System.out.println("Revenue: " + this.revenue + "Profit: " + this.profit);
        System.out.println("Stock name: " + this.associatedAsset);
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
        return this.numberOfStocks * AssetManager.getInstance()
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    private void updateCompanyData() {
        var rand = RandomService.getInstance();
        this.revenue += rand.yieldRandomGaussianNumber(1000, 0);
        this.profit += rand.yieldRandomGaussianNumber(500, 0);
        this.profit = Math.min(this.revenue, this.profit);
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
            GlobalHoldersLock.readUnlock();
        }
    }

    public String getAssociatedAsset() {
        return associatedAsset;
    }
}
