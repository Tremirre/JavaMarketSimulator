package simulation.holders;

import simulation.asset.AssetManager;
import simulation.util.Constants;

public abstract class HolderFactory {
    private static int id = 0;
    private final int currentNumberOfEntities;
    protected final AssetManager assetManager;

    protected HolderFactory(int entitiesCount, AssetManager assetManager) {
        this.currentNumberOfEntities = entitiesCount;
        this.assetManager = assetManager;
    }

    private boolean newThreadExceedsLimit() {
        return currentNumberOfEntities >= Constants.MAX_THREADS;
    }

    protected int useID() {
        if (this.newThreadExceedsLimit())
            return -1;
        return id++;
    }

    public abstract Investor createInvestor();

    public abstract Company createCompany();

    public abstract InvestmentFund createInvestmentFund();
}
