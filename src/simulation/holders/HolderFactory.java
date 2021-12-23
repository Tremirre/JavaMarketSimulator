package simulation.holders;

import simulation.util.Constants;

public abstract class HolderFactory {
    protected static int id = 0;
    protected static int count = 0;
    protected static boolean newThreadExceedsLimit() {
        if (count > Constants.MAX_THREADS)
            return true;
        count++;
        return false;
    }

    public abstract Investor createInvestor();
    public abstract Company createCompany();
    public abstract InvestmentFund createInvestmentFund();
}
