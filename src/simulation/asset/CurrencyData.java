package simulation.asset;

import java.util.ArrayList;

public class CurrencyData extends AssetData {
    private ArrayList<String> countriesOfUse;

    @Override
    public double getLatestSellingPrice() {
        return 0;
    }

    @Override
    public void addLatestSellingPrice(double price) {

    }
}
