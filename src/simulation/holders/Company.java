package simulation.holders;

import simulation.asset.AssetManager;
import simulation.market.Market;
import simulation.market.StockMarket;

public class Company extends AssetHolder {
    private String name;
    private final String IPODate;
    private Address address;
    private double profit;
    private double revenue;
    private final String associatedAsset;
    private int tradingVolume;
    private int totalSales;
    private int numberOfStocks;

    public Company(int id,
                   int numberOfStocks,
                   String name,
                   String IPODate,
                   Address address,
                   double IPOShareValue,
                   double profit,
                   double revenue,
                   int tradingVolume,
                   int totalSales) {
        super(id, 0);
        this.name = name;
        this.IPODate = IPODate;
        this.address = address;
        this.profit = profit;
        this.revenue = revenue; //Optimal strategy: stock price / company profit = 10
        this.tradingVolume = tradingVolume; //number of transactions of stocks of that company
        this.numberOfStocks = numberOfStocks; // Capital = stock price * number of stock
        this.totalSales = totalSales; // total value of transactions of stocks of that company
        StringBuilder stockName = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            if (Character.isLetter(name.charAt(i))) stockName.append(name.charAt(i));
        }
        var stock = AssetManager.getInstance().createStockAsset(stockName.toString().toUpperCase(), IPOShareValue, id);
        this.associatedAsset = stock.getUniqueIdentifyingName();
        this.storedAssets.put(this.associatedAsset, (double) numberOfStocks);
        this.freezeWithdrawal = true;
    }

    public void buyout(StockMarket stockMarket) {

    }

    public void print() {
        System.out.println(this.name);
        this.address.print();
        System.out.println(String.format("IPO date: %s", this.IPODate));
        System.out.println(String.format("Revenue: %f, Profit: %f", this.revenue, this.profit));
        System.out.println(String.format("Stock name: %s", this.associatedAsset));
    }

    public void sendSellOffer(Market market) {
        double price = AssetManager.getInstance().getAssetData(this.associatedAsset).getLatestSellingPrice() * 0.8;
        market.addSellOffer(this.associatedAsset, this, price, this.storedAssets.get(this.associatedAsset));
    }

    public void processSellOrder(String assetType, double price, double amount) {
        this.storedAssets.put(this.associatedAsset, this.storedAssets.get(this.associatedAsset) - amount);
        this.revenue += price;
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
        return this.numberOfStocks * AssetManager.getInstance().getAssetData(this.associatedAsset).getLatestSellingPrice();
    }

    public int getTradingVolume() {
        return tradingVolume;
    }

    public void setTradingVolume(int tradingVolume) {
        this.tradingVolume = tradingVolume;
    }

    public int getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(int totalSales) {
        this.totalSales = totalSales;
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
