package simulation.asset;

import java.util.ArrayList;

abstract public class AssetData {
    final private int id;
    private String name;
    final private double openingPrice;
    private double maximalPrice;
    private double minimalPrice;
    private ArrayList<Double> sellingPrices;

    protected AssetData(int id, String name, double openingPrice) {
        this.id = id;
        this.name = name;
        this.openingPrice = openingPrice;
        this.maximalPrice = openingPrice;
        this.minimalPrice = openingPrice;
        this.sellingPrices = new ArrayList<>();
        this.sellingPrices.add(this.openingPrice);
    }

    abstract public double getLatestSellingPrice();
    abstract public void addLatestSellingPrice(double price);

    public String getUniqueIdentifyingName() {
        return this.name + this.id;
    }

    public double getOpeningPrice() {
        return openingPrice;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaximalPrice() {
        return maximalPrice;
    }

    public double getMinimalPrice() {
        return minimalPrice;
    }
}
