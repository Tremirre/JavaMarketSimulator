package simulation.asset;

public class InformedSupplementaryAssetFactory extends SupplementaryAssetFactory {
    private final String nameOfAsset;
    private final double initialRate;

    public InformedSupplementaryAssetFactory(String nameOfAsset, double rate) {
        this.nameOfAsset = nameOfAsset;
        this.initialRate = rate;
    }

    @Override
    public String createCurrencyAsset() {
        return AssetManager.getInstance()
                .addCurrencyAsset(this.nameOfAsset, this.initialRate)
                .getUniqueIdentifyingName();
    }

    @Override
    public String createCommodityAsset() {
        return AssetManager.getInstance()
                .addCommodityAsset(this.nameOfAsset, this.initialRate)
                .getUniqueIdentifyingName();
    }
}
