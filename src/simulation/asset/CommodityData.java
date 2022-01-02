package simulation.asset;

public class CommodityData extends AssetData{
    private String traidingUnit;
    private String traidingCurrency;

    protected CommodityData(int id, String name, double openingPrice, String tU, String tC) {
        super(id, name, openingPrice);
        this.traidingUnit = tU;
        this.traidingCurrency = tC;
        this.splittable = true;
    }

    @Override
    public double getQualityMeasure() {
        return 0.5;
    }
}
