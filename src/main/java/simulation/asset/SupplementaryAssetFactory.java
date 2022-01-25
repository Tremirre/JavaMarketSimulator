package simulation.asset;

/**
 * Class representing objects creating new assets that are not associated with any outside entities.
 * (e.g. stocks are associated with companies hence they are not created by the SupplementaryAssetFactory)
 */
public abstract class SupplementaryAssetFactory {
    /**
     * Reference to the asset manager.
     */
    protected final AssetManager assetManager;

    SupplementaryAssetFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    /**
     * Creates new currency asset.
     * @return Unique identifying name of the new currency.
     */
    public abstract String createCurrencyAsset();

    /**
     * Creates new commodity asset.
     * @return Unique identifying name of the new commodity.
     */
    public abstract String createCommodityAsset();
}
