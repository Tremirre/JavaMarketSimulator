package simulation.asset;

import java.util.ArrayList;

abstract public class AssetData {
    private String name;
    private double openingPrice;
    private double currentPrice;
    private double maximalPrice;
    private double minimalPrice;
    private ArrayList<Double> sellingPrices;

    abstract public double getLatestSellingPrice();
    abstract public void addLatestSellingPrice(double price);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getOpeningPrice() {
        return openingPrice;
    }

    public void setOpeningPrice(double openingPrice) {
        this.openingPrice = openingPrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getMaximalPrice() {
        return maximalPrice;
    }

    public void setMaximalPrice(double maximalPrice) {
        this.maximalPrice = maximalPrice;
    }

    public double getMinimalPrice() {
        return minimalPrice;
    }

    public void setMinimalPrice(double minimalPrice) {
        this.minimalPrice = minimalPrice;
    }
}
