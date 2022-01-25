package simulation.asset;

import simulation.holders.Company;
import simulation.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This class manages and creates all assets in the simulation.
 */
public final class AssetManager {
    /**
     * Map Holding all assets under their unique identifying names.
     */
    private final HashMap<String, AssetData> allAssets = new HashMap<>();
    /**
     * Latest free assetID.
     */
    private static int assetID = 0;

    /**
     * Creates new Commodity asset and adds it to the pool of all assets.
     * @param name name of the commodity.
     * @param openingPrice initial price per 1 unit of the asset in DEFAULT STANDARD CURRENCY.
     * @param tradingUnit trading unit of the commodity.
     * @param currency currency with which the commodity may be traded.
     * @return new commodity asset.
     */
    public CommodityData addCommodityAsset(String name, double openingPrice, String tradingUnit, String currency) {
        var com = new CommodityData(assetID++, name, openingPrice, tradingUnit, currency);
        this.allAssets.put(com.getUniqueIdentifyingName(), com);
        return com;
    }

    /**
     * Creates new Currency asset and adds it to the pool of all assets.
     * (But does not set the trading unit nor the trading currency of the new commodity)
     * @param name name of the commodity.
     * @param openingPrice initial price per 1 unit of the asset in DEFAULT STANDARD CURRENCY.
     * @return new commodity asset without set trading unit and trading currency.
     */
    public CommodityData addCommodityAsset(String name, double openingPrice) {
        var com = new CommodityData(assetID++, name, openingPrice, null, null);
        this.allAssets.put(com.getUniqueIdentifyingName(), com);
        return com;
    }

    /**
     * Creates new Stock asset and adds it to the pool of all assets.
     * @param name name of the stock.
     * @param openingPrice initial price per 1 unit of the asset in DEFAULT STANDARD CURRENCY.
     * @param company reference to the company with which the asset is associated.
     * @return new stock asset.
     */
    public StockData addStockAsset(String name, double openingPrice, Company company) {
        var stock = new StockData(assetID++, name, openingPrice, company);
        this.allAssets.put(stock.getUniqueIdentifyingName(), stock);
        return stock;
    }

    /**
     * Creates new Currency asset and adds it to the pool of all assets.
     * @param name name of the currency.
     * @param openingPrice initial price per 1 unit of the asset in DEFAULT STANDARD CURRENCY.
     * @param countriesOfUse a set of countries in which the currency is used as a legal tender.
     * @param stability stability factor of the currency that defines its quality. This value can be only in the range
     *                  between 0 and 1.
     * @return new currency asset.
     */
    public CurrencyData addCurrencyAsset(String name, double openingPrice, HashSet<String> countriesOfUse, double stability) {
        var cur = new CurrencyData(assetID++, name, openingPrice, new HashSet<>(countriesOfUse), stability);
        this.allAssets.put(cur.getUniqueIdentifyingName(), cur);
        return cur;
    }

    /**
     * Creates new Currency asset and adds it to the pool of all assets.
     * Initializes its set of countries of use to be empty and sets initial stability to 0.
     * @param name name of the currency.
     * @param openingPrice initial price per 1 unit of the asset in DEFAULT STANDARD CURRENCY.
     * @return new currency asset.
     */
    public CurrencyData addCurrencyAsset(String name, double openingPrice) {
        var cur = new CurrencyData(assetID++, name, openingPrice, new HashSet<>(), 0);
        this.allAssets.put(cur.getUniqueIdentifyingName(), cur);
        return cur;
    }

    /**
     * Given the unique identifying name of the asset returns its asset data.
     * Returns null if non-existent name is passed.
     * @param uniqueName unique name of the asset.
     * @return asset data of the found asset.
     */
    public AssetData getAssetData(String uniqueName) {
        return this.allAssets.getOrDefault(uniqueName, null);
    }

    /**
     * Checks if there exists an asset with given name and of provided type.
     * @param uniqueName unique identifying name of the asset to be checked.
     * @param type type of the asset to be checked.
     * @return boolean denoting whether there exists such an asset.
     */
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

    /**
     * Removes the asset with the provided name from the pool of assets.
     * @param uniqueName unique identifying name of the asset to be removed.
     */
    public void removeAsset(String uniqueName) {
        this.allAssets.remove(uniqueName);
    }

    /**
     * Processes end day events and updated asset prices.
     */
    public void processEndDay() {
        for (var asset : this.allAssets.values()) {
            asset.processDayPrices();
            asset.processAssetEvents();
        }
    }

    /**
     * @return mapping between each of the assets and its opening price.
     */
    public HashMap<String, Double> getAssetOpeningPrices() {
        var prices = new HashMap<String, Double>();
        for (var entry : this.allAssets.entrySet()) {
            prices.put(entry.getKey(), entry.getValue().getOpeningPrice());
        }
        return prices;
    }

    /**
     * @return mapping between each of the assets and its price history.
     */
    public HashMap<String, ArrayList<Double>> getAllAssetsPriceHistory() {
        var prices = new HashMap<String, ArrayList<Double>>();
        for (var entry : this.allAssets.entrySet()) {
            prices.put(entry.getKey(), entry.getValue().getPriceHistory());
        }
        return prices;
    }

    /**
     * Finds the currency by name (returns null if it has not been found).
     * @param currencyName unique identifying name of the queried currency.
     * @return currency data with queried unique identifying name.
     */
    public CurrencyData findCurrencyByName(String currencyName) {
        var data = this.getAssetData(currencyName);
        if (!(data instanceof CurrencyData))
            return null;
        return (CurrencyData) data;
    }

    /**
     * Returns the latest price of the currency with provided name.
     * Returns 1.0 if the queried currency is the DEFAULT TRADING CURRENCY.
     * @param currencyName unique identifying name of the queried currency.
     * @return opening price of that currency.
     */
    public double findPrice(String currencyName) {
        return currencyName.equals(Constants.DEFAULT_CURRENCY) ? 1.0
                : this.getAssetData(currencyName).getOpeningPrice();
    }

    /**
     * Checks if provided asset data is of provided asset category.
     * @param asset asset data.
     * @param category asset category.
     * @return boolean denoting whether the given asset is of given category.
     */
    private boolean isAssetOfCategory(AssetData asset, AssetCategory category) {
        switch (category) {
            case CURRENCY -> {return asset instanceof CurrencyData;}
            case COMMODITY -> {return asset instanceof  CommodityData;}
            case STOCK -> {return asset instanceof StockData;}
            default -> throw new IllegalStateException();
        }
    }

    /**
     * @return all asset types in the simulation.
     */
    public int getNumberOfAssetTypes() {
        return this.allAssets.size();
    }

    /**
     * Given a category, returns all asset types of that category.
     * @param category queried asset category.
     * @return set of unique identifying names of all asset types of the given category.
     */
    public HashSet<String> getAssetsByCategory(AssetCategory category) {
        HashSet<String> filteredAssets = new HashSet<>();
        for (var entry : this.allAssets.entrySet()) {
            if (this.isAssetOfCategory(entry.getValue(), category))
                filteredAssets.add(entry.getKey());
        }
        return filteredAssets;
    }
}
