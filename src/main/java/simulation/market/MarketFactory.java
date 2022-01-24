package simulation.market;

import simulation.asset.AssetCategory;
import simulation.asset.AssetManager;

/**
 * Class tasked with creating new markets.
 */
public abstract class MarketFactory {
    /**
     * Reference to the asset manager.
     */
    protected final AssetManager assetManager;

    /**
     * @param assetManager reference to the asset manager.
     */
    MarketFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    /**
     * Given a category, returns a new market of corresponding type.
     * @param category market category.
     * @return new market of the given category.
     */
    public abstract Market createMarket(AssetCategory category);
}
