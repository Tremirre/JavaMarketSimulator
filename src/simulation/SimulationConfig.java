package simulation;

public class SimulationConfig {
    private static SimulationConfig instance;
    private int maxTransactionsPerDayPerMarket = 200;

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
