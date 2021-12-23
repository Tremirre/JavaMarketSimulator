package simulation;

public class SimulationConfig {
     private int maxTransactionsPerDay = 10;
     final public int MAX_THREADS = 2000;

    public int getMaxTransactionsPerDay() {
        return maxTransactionsPerDay;
    }

    public void setMaxTransactionsPerDay(int maxTransactionsPerDay) {
        this.maxTransactionsPerDay = maxTransactionsPerDay;
    }
}
