package simulation.asset;

public class StockData extends AssetData{
    final private int companyID;

    protected StockData(int id, String name, double openingPrice, int companyID) {
        super(id, name, openingPrice);
        this.companyID = companyID;
    }
}
