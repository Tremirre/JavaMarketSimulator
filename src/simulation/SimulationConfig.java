package simulation;

public class SimulationConfig {
    private static SimulationConfig instance;
    private int maxTransactionsPerDayPerMarket = 100;
    private int maxOfferAge = 20;

    public int getMaxOfferAge() {
        return maxOfferAge;
    }

    public void setMaxOfferAge(int maxOfferAge) {
        this.maxOfferAge = maxOfferAge;
    }

    public int getMaxTransactionsPerDayPerMarket() {
        return maxTransactionsPerDayPerMarket;
    }

    public void setMaxTransactionsPerDayPerMarket(int maxTransactionsPerDayPerMarket) {
        this.maxTransactionsPerDayPerMarket = maxTransactionsPerDayPerMarket;
    }

    public static SimulationConfig getInstance() {
        if (instance == null) {
            instance = new SimulationConfig();
        }
        return instance;
    }
}
