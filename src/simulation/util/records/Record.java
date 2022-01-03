package simulation.util.records;

public abstract class Record {
    private boolean isUsed = false;
    private String name;
    private double initialRate;

    Record(String name, double initialRate) {
        this.name = name;
        this.initialRate = initialRate;
    }

    public boolean isUsed() { return isUsed;}

    public void use() {
        this.isUsed = true;
    }

    public String getName() {
        return name;
    }

    public double getInitialRate() {
        return initialRate;
    }
}
