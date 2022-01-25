package simulation.core;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This singleton class holds all configurable parameters of the simulation/
 */
public final class SimulationConfig {
    /**
     * Held instance of the singleton.
     */
    private static SimulationConfig instance;
    /**
     * Maximal number of transactions that may be processed in one market in one day.
     */
    private final AtomicInteger maxTransactionsPerDayPerMarket = new AtomicInteger(100);
    /**
     * Maximal number of days that a withdrawable offer may be present on the market.
     */
    private final AtomicInteger maxOfferAge = new AtomicInteger(3);
    /**
     * Proportion of the bulls on the market. It is expressed as a probability of an entity sending a buy offer.
     * The proportion of bears is equal to the 1 - bullProportion and is expressed as a probability of an entity
     * sending a sell offer.
     */
    private double bullProportion = 0.5;
    /**
     * Multiplier of all sleep times in the simulation.
     */
    private double timeMultiplier = 1.0;
    /**
     * Proportion of the investors with Naive Investment Strategy on the market.
     */
    private double naiveProportion = 0.7;
    /**
     * Proportion of the investors with Qualitative Assessment Strategy on the market.
     */
    private double qualitativeProportion = 0.2;
    /**
     * Proportion of the investors with Momentum Investment Strategy on the market.
     */
    private double momentumProportion = 1 - naiveProportion - qualitativeProportion;
    /**
     * Boolean flag denoting whether the asset price restoring mechanism is turned on.
     */
    private boolean restoringMechanism = true;

    public int getMaxOfferAge() {
        return maxOfferAge.get();
    }

    /**
     * Atomically sets the max offer age parameter.
     * @param maxOfferAge maximal number of days that a withdrawable offer may be present on the market.
     */
    public void setMaxOfferAge(int maxOfferAge) {
        this.maxOfferAge.getAndSet(maxOfferAge);
    }

    public int getMaxTransactionsPerDayPerMarket() {
        return maxTransactionsPerDayPerMarket.get();
    }

    /**
     * Atomically sets the max transactions per day per market parameter.
     * @param maxTransactionsPerDayPerMarket maximal number of transactions that may be processed in one market in one day.
     */
    public void setMaxTransactionsPerDayPerMarket(int maxTransactionsPerDayPerMarket) {
        this.maxTransactionsPerDayPerMarket.getAndSet(maxTransactionsPerDayPerMarket);
    }

    /**
     * @param restoringMechanism boolean flag denoting whether the asset price restoring mechanism is to be turned on.
     */
    public synchronized void setRestoringMechanism(boolean restoringMechanism) {
        this.restoringMechanism = restoringMechanism;
    }

    public synchronized boolean restoringMechanismEnabled() {
        return this.restoringMechanism;
    }

    public synchronized double getBullProportion() {
        return this.bullProportion;
    }

    public synchronized double getBearProportion() {
        return 1 - this.bullProportion;
    }

    /**
     * @param proportion proportion of the bulls on the market. It is expressed as a probability of an entity sending a buy offer.
     *      * The proportion of bears is equal to the 1 - bullProportion and is expressed as a probability of an entity
     *      * sending a sell offer.
     */
    public synchronized void setBullProportion(double proportion) {
        this.bullProportion = proportion;
    }

    public synchronized static SimulationConfig getInstance() {
        if (instance == null) {
            instance = new SimulationConfig();
        }
        return instance;
    }

    public synchronized double getTimeMultiplier() {
        return this.timeMultiplier;
    }

    /**
     * Sets the time multiplier, ensuring it is not smaller than 1.
     * @param timeMultiplier multiplier of all sleep times in the simulation.
     */
    public synchronized void setTimeMultiplier(double timeMultiplier) {
        this.timeMultiplier = Math.max(1, timeMultiplier);
    }

    public double getNaiveProportion() {
        return naiveProportion;
    }

    public double getQualitativeProportion() {
        return qualitativeProportion;
    }

    public double getMomentumProportion() {
        return momentumProportion;
    }

    /**
     * Sets respective proportions of the investors with specified strategies on the market, ensuring they all
     * sum to 1. In case all are set to 0, the proportions will be set to 1/3 for every proportion.
     * @param naiveProportion proportion of the investors with Naive Investment Strategy on the market.
     * @param qualitativeProportion proportion of the investors with Qualitative Assessment Strategy on the market.
     * @param momentumProportion proportion of the investors with Momentum Investment Strategy on the market.
     */
    public void setProportions(double naiveProportion, double qualitativeProportion, double momentumProportion) {
        double total = naiveProportion + qualitativeProportion + momentumProportion;
        if(total != 0) {
            this.naiveProportion = naiveProportion / total;
            this.qualitativeProportion = qualitativeProportion / total;
            this.momentumProportion = momentumProportion / total;
        } else {
            this.naiveProportion = 1.0/3;
            this.qualitativeProportion = 1.0/3;
            this.momentumProportion = 1 - this.naiveProportion - this.qualitativeProportion;
        }
    }
}
