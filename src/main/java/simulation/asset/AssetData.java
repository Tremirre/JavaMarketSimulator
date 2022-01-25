package simulation.asset;

import simulation.core.SimulationConfig;
import simulation.util.Constants;

import java.util.ArrayList;

/**
 * Base class representing the common functionality of every asset.
 */
abstract public class AssetData {
    /**
     * Asset identifier.
     */
    final private int id;
    /**
     * Asset name.
     */
    private String name;
    /**
     * Opening price of the day for 1 unit of the asset in DEFAULT STANDARD CURRENCY.
     */
    private double openingPrice;
    /**
     * Maximal recorded price for 1 unit of the asset in DEFAULT STANDARD CURRENCY.
     */
    private double maximalPrice;
    /**
     * Minimal recorded price for 1 unit of the asset in DEFAULT STANDARD CURRENCY.
     */
    private double minimalPrice;
    /**
     * History of asset prices for every day of the simulation since the asset creation.
     */
    private ArrayList<Double> sellingPrices;
    /**
     * List aggregating prices of transactions made in the given day of the asset.
     */
    private ArrayList<Double> salesBuffer;
    /**
     * Flag denoting whether the asset is discrete (e.g. stocks) or continuous (e.g. currency).
     */
    protected boolean splittable;
    /**
     * Value denoting the left days of the restoring effect.
     */
    private int priceRestoringEffectDaysLeft = 0;
    /**
     * Value denoting the price change during the restoring effect. (Initially 1 denoting no change).
     */
    private double priceRestoringMultiplier = 1.0;

    /**
     * Initializes containers, sets the min and max price as the provided opening price, adds the opening price to the
     * asset price history.
     * @param id id of the asset.
     * @param name name of the asset.
     * @param openingPrice initial value of the asset per 1 unit in DEFAULT STANDARD CURRENCY.
     */
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

    /**
     * Adds transaction price to the buffer and updates min/max price if needed.
     * @param price
     */
    public void addLatestSellingPrice(double price) {
        this.salesBuffer.add(price);
        if (this.maximalPrice < price)
            this.maximalPrice = price;
        if (this.minimalPrice > price)
            this.minimalPrice = price;
    }

    /**
     * Adds the average of the day's transaction prices to the price history. (Adds the opening price if no transactions
     * have been made in a day)
     */
    public void processDayPrices() {
        double averagePrice = this.salesBuffer.isEmpty() ? this.getOpeningPrice() : 0;
        for (var price : this.salesBuffer)
            averagePrice += price / this.salesBuffer.size();
        this.sellingPrices.add(averagePrice);
        this.openingPrice = averagePrice;
        this.salesBuffer.clear();
    }

    /**
     * Returns the unique identifier of the asset as the asset's name concatenated by its numerical id.
     * @return identifying name of the asset.
     */
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

    /**
     * Processes asset random and non-random events.
     * Checks and applies restoring effect if needed.
     */
    public void processAssetEvents() {
        if (SimulationConfig.getInstance().restoringMechanismEnabled()
                && this.sellingPrices.size() > 10
                && this.priceRestoringEffectDaysLeft == 0) {
            var initialPrice = this.sellingPrices.get(0);
            double proportion = this.getOpeningPrice()/initialPrice;
            this.priceRestoringEffectDaysLeft = Constants.RESTORING_EFFECT_DURATION;
            if (proportion > Constants.RESTORING_EFFECT_PROPORTION) {
                this.priceRestoringMultiplier = 1.0 - Constants.RESTORING_EFFECT_CHANGE;
            } else if (proportion < 1.0/Constants.RESTORING_EFFECT_PROPORTION) {
                this.priceRestoringMultiplier = 1.0 + Constants.RESTORING_EFFECT_CHANGE;
            } else {
                this.priceRestoringMultiplier = 1.0;
                this.priceRestoringEffectDaysLeft = 0;
            }
        }
        if (this.priceRestoringEffectDaysLeft > 0) {
            this.priceRestoringEffectDaysLeft--;
            double newPrice = this.sellingPrices.get(this.sellingPrices.size() - 1) * this.priceRestoringMultiplier;
            this.sellingPrices.set(this.sellingPrices.size() - 1, newPrice);
            this.openingPrice = newPrice;
        }
    }
}
