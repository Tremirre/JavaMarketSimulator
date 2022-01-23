package simulation.asset;

import simulation.core.SimulationConfig;
import simulation.util.Constants;
import simulation.util.RandomService;

import java.util.ArrayList;
import java.util.Random;

abstract public class AssetData {
    final private int id;
    private String name;
    private double openingPrice;
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

    public double getOpeningPrice() {
        return this.sellingPrices.get(this.sellingPrices.size() - 1);
    }

    public abstract double getQualityMeasure();

    public boolean isSplittable() {
        return this.splittable;
    }

    public void addLatestSellingPrice(double price) {
        this.salesBuffer.add(price);
        if (this.maximalPrice < price)
            this.maximalPrice = price;
        if (this.minimalPrice > price)
            this.minimalPrice = price;
    }

    public void processDayPrices() {
        double averagePrice = this.salesBuffer.isEmpty() ? this.getOpeningPrice() : 0;
        for (var price : this.salesBuffer)
            averagePrice += price / this.salesBuffer.size();
        this.sellingPrices.add(averagePrice);
        this.openingPrice = averagePrice;
        this.salesBuffer.clear();
    }

    public String getUniqueIdentifyingName() {
        return this.name + this.id;
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

    public void processRandomEvent() {
        if (SimulationConfig.getInstance().restoringMechanismEnabled() && this.sellingPrices.size() > 10) {
            var initialPrice = this.sellingPrices.get(0);
            double proportion = this.getOpeningPrice()/initialPrice;
            double multiplier = 1;
            multiplier += proportion > Constants.RESTORING_EFFECT_PROPORTION ? -0.05 : 0;
            multiplier += proportion < 1.0/Constants.RESTORING_EFFECT_PROPORTION ? 0.05 : 0;
            double newPrice = this.sellingPrices.get(this.sellingPrices.size() - 1) * multiplier;
            this.sellingPrices.set(this.sellingPrices.size() - 1, newPrice);
            this.openingPrice = newPrice;
        }
    }
}
