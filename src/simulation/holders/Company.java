package simulation.holders;

import simulation.asset.AssetManager;
import simulation.holders.strategies.PassiveCompanyStrategy;
import simulation.market.StockMarket;

public class Company extends AssetHolder {
    private String name;
    private final String IPODate;
    private Address address;
    private double profit;
    private double revenue;
    private final String associatedAsset;
    private int dailyTradingVolume;
    private double dailyTotalSales;
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
        this.revenue = revenue; //Optimal strategy: stock price / company profit = 10
        this.dailyTradingVolume = 0; //number of transactions of stocks of that company
        this.numberOfStocks = numberOfStocks; // Capital = stock price * number of stock
        this.dailyTotalSales = 0; // total value of transactions of stocks of that company
        StringBuilder stockName = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            if (Character.isLetter(name.charAt(i))) stockName.append(name.charAt(i));
        }
        var stock = AssetManager.getInstance().addStockAsset(stockName.toString().toUpperCase(), IPOShareValue, id);
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
        return this.numberOfStocks * AssetManager.getInstance().getAssetData(this.associatedAsset).getLatestAverageSellingPrice();
    }

    public synchronized void addToVolume(double amount) {
        this.dailyTradingVolume += amount;
    }

    public synchronized void addToSales(double price) {
        this.dailyTotalSales += price;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public void run() {

    }

    public String getAssociatedAsset() {
        return associatedAsset;
    }
}
