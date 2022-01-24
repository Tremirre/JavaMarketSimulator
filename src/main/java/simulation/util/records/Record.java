package simulation.util.records;

/**
 * Class for holding imported information about real-world commodities and currencies.
 */
public abstract class Record {
    /**
     * Boolean denoting whether the asset has been already used by the simulation
     */
    private boolean isUsed = false;
    private String name;
    private double initialRate;

    /**
     * Constructor for base Record class that fills name and initial rate with provided values.
     * @param name Name of the asset.
     * @param initialRate Initial rate of the asset.
     */
    Record(String name, double initialRate) {
        this.name = name;
        this.initialRate = initialRate;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void use() {
        this.isUsed = true;
    }

    public void unUse() { this.isUsed = false; }

    public String getName() {
        return name;
    }

    public double getInitialRate() {
        return initialRate;
    }
}
