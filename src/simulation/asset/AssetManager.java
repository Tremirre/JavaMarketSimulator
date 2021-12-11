package simulation.asset;

import java.util.HashMap;

public class AssetManager {
    private static AssetManager instance;
    private HashMap<String,AssetData> allAssets;
    private static int commodityID = 0;
    private static int currencyID = 0;
    private static int stockID = 0;


    private AssetManager() {
        this.allAssets = new HashMap<String, AssetData>();
    }

    public static AssetManager getInstance() {
        if (instance == null) {
            instance = new AssetManager();
        }
        return instance;
    }

    public CommodityData createCommodityAsset(String name, double openingPrice, String tradingUnit, String currency) {
        var com = new CommodityData(commodityID++, name, openingPrice, tradingUnit, currency);
        this.allAssets.put(com.getUniqueIdentifyingName(), com);
        return com;
    }

    public StockData createStockAsset(String name, double openingPrice, int companyID) {
        var stock = new StockData(stockID++, name, openingPrice, companyID);
        this.allAssets.put(stock.getUniqueIdentifyingName(), stock);
        return stock;
    }

    public CurrencyData createCurrencyAsset(String name, double openingPrice, String[] countriesOfUse) {
        var cur = new CurrencyData(currencyID++, name, openingPrice, countriesOfUse);
        this.allAssets.put(cur.getUniqueIdentifyingName(), cur);
        return cur;
    }

    public AssetData getAssetData(String uniqueName){
        return this.allAssets.getOrDefault(uniqueName, null);
    }

    public boolean doesAssetExist(String uniqueName, AssetType type) {
        switch (type) {
            case STOCK -> {
                return this.allAssets.containsKey(uniqueName) && this.allAssets.get(uniqueName) instanceof StockData;
            }
            case CURRENCY -> {
                return this.allAssets.containsKey(uniqueName) && this.allAssets.get(uniqueName) instanceof CurrencyData;
            }
            case COMMODITY -> {
                return this.allAssets.containsKey(uniqueName) && this.allAssets.get(uniqueName) instanceof CommodityData;
            }
            default -> {
                //throw exception
            }
        }
        return false;
    }

    public boolean doesAssetExist(String uniqueName) {
        return this.allAssets.containsKey(uniqueName);
    }

    public void removeAsset(String uniqueName) {
        this.allAssets.remove(uniqueName);
    }
}
