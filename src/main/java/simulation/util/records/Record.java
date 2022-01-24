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

    /**
     * Checks if asset is used.
     * @return used flag.
     */
    public boolean isUsed() {
        return isUsed;
    }

    /**
     * Marks the asset as used.
     */
    public void use() {
        this.isUsed = true;
    }

    /**
     * Marks the asset as unused.
     */
    public void unUse() { this.isUsed = false; }

    public String getName() {
        return name;
    }

    public double getInitialRate() {
        return initialRate;
    }
}
