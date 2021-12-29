package simulation;

import java.util.concurrent.atomic.AtomicInteger;

public class SimulationConfig {
    private static SimulationConfig instance;
    private AtomicInteger maxTransactionsPerDayPerMarket = new AtomicInteger(100);
    private AtomicInteger maxOfferAge = new AtomicInteger(20);

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

    public static SimulationConfig getInstance() {
        if (instance == null) {
            instance = new SimulationConfig();
        }
        return instance;
    }
}
