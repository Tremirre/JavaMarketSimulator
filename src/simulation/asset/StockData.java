package simulation.asset;

public class StockData extends AssetData{
    private String companyID;

    @Override
    public double getLatestSellingPrice() {
        return 0;
    }

    @Override
    public void addLatestSellingPrice(double price) {

    }
}
