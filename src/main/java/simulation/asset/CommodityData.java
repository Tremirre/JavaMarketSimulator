package simulation.asset;

/**
 * Subclass of AssetData representing data associated with a commodity.
 */
public class CommodityData extends AssetData {
    /**
     * Name of 1 unit of the commodity.
     */
    private String tradingUnit;
    /**
     * Unique identifying name of the currency with which the commodity may be traded.
     */
    private String tradingCurrency;

    /**
     * Fills the asset with data.
     * @param id numeric id of the commodity.
     * @param name name of the commodity.
     * @param openingPrice initial price of the commodity per 1 unit of it in DEFAULT STANDARD CURRENCY.
     * @param tU name of 1 unit of the commodity.
     * @param tC unique identifying name of the currency in which the commodity may be traded.
     */
    protected CommodityData(int id, String name, double openingPrice, String tU, String tC) {
        super(id, name, openingPrice);
        this.tradingUnit = tU;
        this.tradingCurrency = tC;
        this.splittable = true;
    }

    /**
     * {@inheritDoc}
     * For commodities the quality measure is fixed to be 0.5.
     * @return
     */
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
