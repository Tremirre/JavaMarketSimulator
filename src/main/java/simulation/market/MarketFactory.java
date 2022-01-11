package simulation.market;

import simulation.asset.AssetManager;

public abstract class MarketFactory {
    protected final AssetManager assetManager;

    MarketFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public abstract Market createMarket(MarketType type);
}
