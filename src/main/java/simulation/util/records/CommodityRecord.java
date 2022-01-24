package simulation.util.records;

/**
 * Class dedicated to holding a single commodity
 */
public class CommodityRecord extends Record {
    private String unit;

    public CommodityRecord(String name, double initialRate, String unit) {
        super(name, initialRate);
        this.unit = unit;
    }

    public String getUnit() {
        return this.unit;
    }
}
