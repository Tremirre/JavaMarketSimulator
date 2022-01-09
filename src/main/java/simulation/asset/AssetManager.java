package main.java.simulation.asset;

import main.java.simulation.holders.Company;
import main.java.simulation.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public final class AssetManager {
    private final HashMap<String, AssetData> allAssets = new HashMap<>();
    private static int assetID = 0;

    public CommodityData addCommodityAsset(String name, double openingPrice, String tradingUnit, String currency) {
        var com = new CommodityData(assetID++, name, openingPrice, tradingUnit, currency);
        this.allAssets.put(com.getUniqueIdentifyingName(), com);
        return com;
    }

    public CommodityData addCommodityAsset(String name, double openingPrice) {
        var com = new CommodityData(assetID++, name, openingPrice, null, null);
        this.allAssets.put(com.getUniqueIdentifyingName(), com);
        return com;
    }

    public StockData addStockAsset(String name, double openingPrice, Company company) {
        var stock = new StockData(assetID++, name, openingPrice, company);
        this.allAssets.put(stock.getUniqueIdentifyingName(), stock);
        return stock;
    }

    public CurrencyData addCurrencyAsset(String name, double openingPrice, HashSet<String> countriesOfUse, double stability) {
        var cur = new CurrencyData(assetID++, name, openingPrice, new HashSet<>(countriesOfUse), stability);
        this.allAssets.put(cur.getUniqueIdentifyingName(), cur);
        return cur;
    }

    public CurrencyData addCurrencyAsset(String name, double openingPrice) {
        var cur = new CurrencyData(assetID++, name, openingPrice, new HashSet<>(), 0);
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
                throw new IllegalStateException("Invalid asset category passed to the asset manager: " + type);
            }
        }
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
            asset.processRandomEvent();
        }
    }

    public HashMap<String, Double> getLatestAverageAssetPrices() {
        var prices = new HashMap<String, Double>();
        for (var entry : this.allAssets.entrySet()) {
            prices.put(entry.getKey(), entry.getValue().getOpeningPrice());
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

    public CurrencyData findCurrencyByName(String currencyName) {
        for (var data : this.allAssets.values()) {
            if (data instanceof CurrencyData && data.getName().equals(currencyName)) {
                return (CurrencyData) data;
            }
        }
        return null;
    }

    public double findPrice(String currencyName) {
        return currencyName.equals(Constants.DEFAULT_CURRENCY) ? 1.0
                : this.getAssetData(currencyName).getOpeningPrice();
    }
}
