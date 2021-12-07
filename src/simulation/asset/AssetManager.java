package simulation.asset;

import java.util.ArrayList;
import java.util.HashMap;

public class AssetManager {
    private HashMap<String,AssetData> allAssets;

    public void addAsset(AssetData data) {

    }

    public void createCommodityAsset(String name,
                                     double openingPrice,
                                     double currentPrice,
                                     double maxPrice,
                                     double minPrice,
                                     String traidingUnit,
                                     String currency) {

    }

    public void createCurrencyAsset(String name,
                                    double openingPrice,
                                    double currentPrice,
                                    double maxPrice,
                                    double minPrice,
                                    String companyID) {

    }

    public void createStockAsset(String name,
                                 double openingPrice,
                                 double currentPrice,
                                 double maxPrice,
                                 double minPrice,
                                 ArrayList<String> countriesOfUse) {

    }
    public AssetData getAssetData(String name){
        return null;
    }
}
