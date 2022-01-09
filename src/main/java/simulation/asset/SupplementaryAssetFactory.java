package main.java.simulation.asset;

public abstract class SupplementaryAssetFactory {
    protected final AssetManager assetManager;

    SupplementaryAssetFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public abstract String createCurrencyAsset();
    public abstract String createCommodityAsset();
}
