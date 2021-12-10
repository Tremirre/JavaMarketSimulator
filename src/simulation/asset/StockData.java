package simulation.asset;

public class StockData extends AssetData{
    final private int companyID;

    protected StockData(int id, String name, double openingPrice, int companyID) {
        super(id, name, openingPrice);
        this.companyID = companyID;
    }

    @Override
    public double getLatestSellingPrice() {
        return 0;
    }

    @Override
    public void addLatestSellingPrice(double price) {

    }
}
