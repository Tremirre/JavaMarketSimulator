package simulation.market;

import simulation.asset.AssetHolder;

public class Company extends AssetHolder {
    private String name;
    private String IPODate;
    private double IPOShareValue;
    private double profit;
    private double revenue;
    private double capital;
    private int tradingVolume;
    private int totalSales;

    public void buyout(StockMarket stockMarket) {

    }

    public String getCompanyName() {
        return name;
    }

    public void setCompnayName(String name) {
        this.name = name;
    }

    public String getIPODate() {
        return IPODate;
    }

    public void setIPODate(String IPODate) {
        this.IPODate = IPODate;
    }

    public double getIPOShareValue() {
        return IPOShareValue;
    }

    public void setIPOShareValue(double IPOShareValue) {
        this.IPOShareValue = IPOShareValue;
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
        return capital;
    }

    public void setCapital(double capital) {
        this.capital = capital;
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
}
