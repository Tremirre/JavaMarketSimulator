package simulation.core;

import java.util.concurrent.atomic.AtomicInteger;

public final class SimulationConfig {
    private static SimulationConfig instance;
    private AtomicInteger maxTransactionsPerDayPerMarket = new AtomicInteger(100);
    private AtomicInteger maxOfferAge = new AtomicInteger(10);
    private double bullProportion = 0.5;

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
}
