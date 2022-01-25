package simulation.asset;

/**
 * This class represents objects that create new asset (not associated with any trading entity) based on provided data.
 */
public class InformedSupplementaryAssetFactory extends SupplementaryAssetFactory {
    private final String nameOfAsset;
    private final double initialRate;

    /**
     * Initializes the factory with the data of the new currency.
     * @param assetManager reference to the asset manager.
     * @param nameOfAsset name of the asset to be created.
     * @param rate initial price of the new asset per 1 unit of it in DEFAULT STANDARD CURRENCY.
     */
    public InformedSupplementaryAssetFactory(AssetManager assetManager, String nameOfAsset, double rate) {
        super(assetManager);
        this.nameOfAsset = nameOfAsset;
        this.initialRate = rate;
    }

    /**
     * Creates new asset currency through the asset manager.
     * @return unique identifying name of the new currency.
     */
    @Override
    public String createCurrencyAsset() {
        return this.assetManager
                .addCurrencyAsset(this.nameOfAsset, this.initialRate)
                .getUniqueIdentifyingName();
    }

    /**
     * Creates new commodity through the asset manager.
     * @return unique identifying name of the new commodity.
     */
    @Override
    public String createCommodityAsset() {
        return this.assetManager
                .addCommodityAsset(this.nameOfAsset, this.initialRate)
                .getUniqueIdentifyingName();
    }
}
