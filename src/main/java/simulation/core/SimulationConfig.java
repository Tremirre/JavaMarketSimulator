package simulation.core;

import java.util.concurrent.atomic.AtomicInteger;

public final class SimulationConfig {
    private static SimulationConfig instance;
    private final AtomicInteger maxTransactionsPerDayPerMarket = new AtomicInteger(100);
    private final AtomicInteger maxOfferAge = new AtomicInteger(10);
    private double bullProportion = 0.5;
    private double timeMultiplier = 1.0;
    private double naiveProportion = 2.0/3;
    private double qualitativeProportion = 1.0/6;
    private double momentumProportion = 1 - naiveProportion - qualitativeProportion;

    public int getMaxOfferAge() {
        return maxOfferAge.get();
    }

    public void setMaxOfferAge(int maxOfferAge) {
        this.maxOfferAge.getAndSet(maxOfferAge);
    }

    public int getMaxTransactionsPerDayPerMarket() {
        return maxTransactionsPerDayPerMarket.get();
    }

    public void setMaxTransactionsPerDayPerMarket(int maxTransactionsPerDayPerMarket) {
        this.maxTransactionsPerDayPerMarket.getAndSet(maxTransactionsPerDayPerMarket);
    }

    public synchronized double getBullProportion() {
        return this.bullProportion;
    }

    public synchronized double getBearProportion() {
        return 1 - this.bullProportion;
    }

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
