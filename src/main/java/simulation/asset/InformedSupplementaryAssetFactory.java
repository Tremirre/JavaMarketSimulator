package main.java.simulation.asset;

public class InformedSupplementaryAssetFactory extends SupplementaryAssetFactory {
    private final String nameOfAsset;
    private final double initialRate;

    public InformedSupplementaryAssetFactory(AssetManager assetManager, String nameOfAsset, double rate) {
        super(assetManager);
        this.nameOfAsset = nameOfAsset;
        this.initialRate = rate;
    }

    @Override
    public String createCurrencyAsset() {
        return this.assetManager
                .addCurrencyAsset(this.nameOfAsset, this.initialRate)
                .getUniqueIdentifyingName();
    }

    @Override
    public String createCommodityAsset() {
        return this.assetManager
                .addCommodityAsset(this.nameOfAsset, this.initialRate)
                .getUniqueIdentifyingName();
    }
}
