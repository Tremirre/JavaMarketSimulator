package simulation.asset;

import java.util.HashMap;

public class AssetManager {
    private static AssetManager instance;
    private HashMap<String,AssetData> allAssets;
    private static int commodityID = 0;
    private static int currencyID = 0;
    private static int stockID =0;


    private AssetManager() {
        this.allAssets = new HashMap<String, AssetData>();
    }

    public static AssetManager getInstance() {
        if (instance == null) {
            instance = new AssetManager();
        }
        return instance;
    }

    public void createCommodityAsset(String name, double openingPrice, String tradingUnit, String currency) {
        var com = new CommodityData(commodityID++, name, openingPrice, tradingUnit, currency);
        this.allAssets.put(com.getUniqueIndetifyingName(), com);
    }

    public void createStockAsset(String name, double openingPrice, int companyID) {
        var stock = new StockData(stockID++, name, openingPrice, companyID);
        this.allAssets.put(stock.getUniqueIndetifyingName(), stock);
    }

    public void createCurrencyAsset(String name, double openingPrice, String[] countriesOfUse) {
        var cur = new CurrencyData(currencyID++, name, openingPrice, countriesOfUse);
        this.allAssets.put(cur.getUniqueIndetifyingName(), cur);
    }

    public AssetData getAssetData(String uniqueName){
        return this.allAssets.getOrDefault(uniqueName, null);
    }

    public boolean doesAssetExist(String uniqueName, AssetType type) {
        switch (type) {
            case any -> {
                return this.allAssets.containsKey(uniqueName);
            }
            case stock -> {
                return this.allAssets.containsKey(uniqueName) && this.allAssets.get(uniqueName) instanceof StockData;
            }
            case currency -> {
                return this.allAssets.containsKey(uniqueName) && this.allAssets.get(uniqueName) instanceof CurrencyData;
            }
            case commodity -> {
                return this.allAssets.containsKey(uniqueName) && this.allAssets.get(uniqueName) instanceof CommodityData;
            }
        }
        return false;
    }
}
