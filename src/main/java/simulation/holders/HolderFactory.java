package simulation.holders;

import simulation.asset.AssetManager;
import simulation.util.Constants;

/**
 * Class tasked with creating new entities and giving out the free ids.
 */
public abstract class HolderFactory {
    /**
     * Latest free id, initially 0.
     */
    private static int id = 0;
    /**
     * Number of dynamic entities (threads) in the simulation.
     */
    private final int currentNumberOfEntities;
    /**
     * Reference to the asset manager.
     */
    protected final AssetManager assetManager;

    /**
     * Initializes fields with provided values and objects.
     * @param entitiesCount number of dynamic entities (threads) in the simulation.
     * @param assetManager reference to the asset manager.
     */
    protected HolderFactory(int entitiesCount, AssetManager assetManager) {
        this.currentNumberOfEntities = entitiesCount;
        this.assetManager = assetManager;
    }

    /**
     * Checks if creating new thread will break the limit of maximal number of threads that may be in the simulation.
     * @return flag denoting whether the creation of new entity passes the limit.
     */
    private boolean newThreadExceedsLimit() {
        return currentNumberOfEntities >= Constants.MAX_THREADS;
    }

    /**
     * Checks if new id can be used.
     * In case it can, the function passes the latest free id and increments it.
     * If new thread will pass the limit of maximal number of threads, it returns -1 denoting
     * new entity may not be created.
     * @return latest free id.
     */
    protected int useID() {
        if (this.newThreadExceedsLimit())
            return -1;
        return id++;
    }

    /**
     * Creates a new Investor entity.
     * @return new Investor.
     */
    public abstract Investor createInvestor();

    /**
     * Creates a new Company entity.
     * @return new Company.
     */
    public abstract Company createCompany();

    /**
     * Creates a new InvestmentFund entity.
     * @return new InvestmentFund.
     */
    public abstract InvestmentFund createInvestmentFund();
}
