package simulation.asset;

public class CommodityData extends AssetData{
    private String traidingUnit;
    private String traidingCurrency;


    @Override
    public double getLatestSellingPrice() {
        return 0;
    }

    @Override
    public void addLatestSellingPrice(double price) {

    }
}
