package simulation.core;

import java.util.concurrent.atomic.AtomicInteger;

public final class SimulationConfig {
    private static SimulationConfig instance;
    private final AtomicInteger maxTransactionsPerDayPerMarket = new AtomicInteger(100);
    private final AtomicInteger maxOfferAge = new AtomicInteger(10);
    private double bullProportion = 0.5;
    private double timeMultiplier = 1.0;

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
}
