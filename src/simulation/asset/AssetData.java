package simulation.asset;

import java.util.ArrayList;

abstract public class AssetData {
    final private int id;
    private String name;
    final private double openingPrice;
    private double maximalPrice;
    private double minimalPrice;
    private ArrayList<Double> sellingPrices;
    private ArrayList<Double> salesBuffer;
    protected boolean splittable;

    protected AssetData(int id, String name, double openingPrice) {
        this.id = id;
        this.name = name;
        this.openingPrice = openingPrice;
        this.maximalPrice = openingPrice;
        this.minimalPrice = openingPrice;
        this.sellingPrices = new ArrayList<>();
        this.sellingPrices.add(this.openingPrice);
        this.salesBuffer = new ArrayList<>();
    }

    public ArrayList<Double> getPriceHistory() {
        return this.sellingPrices;
    }

    public double getLatestAverageSellingPrice() {
        return this.sellingPrices.get(this.sellingPrices.size() - 1);
    }

    public boolean isSplittable() { return this.splittable; }

    public void addLatestSellingPrice(double price) {
        this.salesBuffer.add(price);
        if (this.maximalPrice < price)
            this.maximalPrice = price;
        if (this.minimalPrice > price)
            this.minimalPrice = price;
    }

    public void processDayPrices() {
        double averagePrice = this.salesBuffer.isEmpty() ? this.getLatestAverageSellingPrice() : 0;
        for (var price : this.salesBuffer)
            averagePrice += price/this.salesBuffer.size();
        /*
        if (averagePrice < openingPrice/10)
            averagePrice *= 2;*/
        this.sellingPrices.add(averagePrice);
        this.salesBuffer.clear();
    }

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
