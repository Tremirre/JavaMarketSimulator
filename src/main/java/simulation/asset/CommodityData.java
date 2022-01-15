package simulation.asset;

public class CommodityData extends AssetData {
    private String tradingUnit;
    private String tradingCurrency;

    protected CommodityData(int id, String name, double openingPrice, String tU, String tC) {
        super(id, name, openingPrice);
        this.tradingUnit = tU;
        this.tradingCurrency = tC;
        this.splittable = true;
    }

    @Override
    public double getQualityMeasure() {
        return 0.5;
    }

    public String getTradingCurrency() {
        return this.tradingCurrency;
    }

    public String getTradingUnit() {
        return this.tradingUnit;
    }

    public void setTradingCurrency(String currency) {
        this.tradingCurrency = currency;
    }

    public void setTradingUnit(String unit) {
        this.tradingUnit = unit;
    }
}
