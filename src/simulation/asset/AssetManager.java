package simulation.asset;

import simulation.holders.Company;

import java.util.ArrayList;
import java.util.HashMap;

public class AssetManager {
    private static AssetManager instance;
    private HashMap<String,AssetData> allAssets;
    private static int assetID = 0;


    private AssetManager() {
        this.allAssets = new HashMap<>();
    }

    public synchronized static AssetManager getInstance() {
        if (instance == null) {
            instance = new AssetManager();
        }
        return instance;
    }

    public CommodityData addCommodityAsset(String name, double openingPrice, String tradingUnit, String currency) {
        var com = new CommodityData(assetID++, name, openingPrice, tradingUnit, currency);
        this.allAssets.put(com.getUniqueIdentifyingName(), com);
        return com;
    }

    public StockData addStockAsset(String name, double openingPrice, Company company) {
        var stock = new StockData(assetID++, name, openingPrice, company);
        this.allAssets.put(stock.getUniqueIdentifyingName(), stock);
        return stock;
    }

    public CurrencyData addCurrencyAsset(String name, double openingPrice, String[] countriesOfUse, double stability) {
        var cur = new CurrencyData(assetID++, name, openingPrice, countriesOfUse, stability);
        this.allAssets.put(cur.getUniqueIdentifyingName(), cur);
        return cur;
    }

    public AssetData getAssetData(String uniqueName){
        return this.allAssets.getOrDefault(uniqueName, null);
    }

    public boolean doesAssetExist(String uniqueName, AssetCategory type) {
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

    public void processEndDay() {
        for (var asset : this.allAssets.values()) {
            asset.processDayPrices();
        }
    }

    public HashMap<String, Double> getLatestAverageAssetPrices() {
        var prices = new HashMap<String, Double>();
        for (var entry : this.allAssets.entrySet()) {
            prices.put(entry.getKey(), entry.getValue().getLatestAverageSellingPrice());
        }
        return prices;
    }

    public HashMap<String, ArrayList<Double>> getAllAssetsPriceHistory() {
        var prices = new HashMap<String, ArrayList<Double>>();
        for (var entry : this.allAssets.entrySet()) {
            prices.put(entry.getKey(), entry.getValue().getPriceHistory());
        }
        return prices;
    }
}
