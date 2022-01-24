package simulation.util;

/**
 * Class for holding all necessary constants within the application.
 */
public final class Constants {
    /**
     * Denotes the maximal number of threads that may be spawned by the application.
     */
    public static final int MAX_THREADS = 2000;
    /**
     * Base trading time in ms.
     */
    public static final int BASE_TRADING_TIME = 10;
    /**
     * Base investor sleep time in ms.
     */
    public static final int BASE_INVESTOR_SLEEP_TIME = 5;
    /**
     * Investor sleep time deviation in ms (for normal number generation).
     */
    public static final int INVESTOR_SLEEP_TIME_DEVIATION = 2;
    public static final int YEAR = 365;
    /**
     * Company sleep time in ms.
     */
    public static final int COMPANY_SLEEP_TIME = 50;
    /**
     * Base investment fund sleep time in ms.
     */
    public static final int BASE_INVESTMENT_FUND_SLEEP_TIME = 10;
    /**
     * Investment fund max additional sleep time in ms (for uniform number generation).
     */
    public static final int INVESTMENT_FUND_MAXIMAL_ADDITIONAL_SLEEP_TIME = 4;
    /**
     * Investment fund mean initial funds (in DEFAULT STANDARD CURRENCY) (for normal number generation).
     */
    public static final int INVESTMENT_FUND_MEAN_INITIAL_FUNDS = 1000;
    /**
     * Investment fund initial funds deviation (in DEFAULT STANDARD CURRENCY) (for normal number generation).
     */
    public static final int INVESTMENT_FUND_INITIAL_FUNDS_DEVIATION = 100;
    /**
     * Constant that serves as a balancing measure when determining the IPO share value and the number of shares
     * Send to the market when creating a random company.
     */
    public static final int COMPANY_GENERATION_CONSTANT = 500;
    /**
     * Minimal company revenue (in DEFAULT STANDARD CURRENCY) (for uniform number generation).
     */
    public static final int COMPANY_MINIMAL_REVENUE = 20000;
    /**
     * Maximal company additional revenue (in DEFAULT STANDARD CURRENCY) (for uniform number generation).
     */
    public static final int COMPANY_MAXIMAL_ADDITIONAL_REVENUE = 20000;
    /**
     * Minimal company profit (in DEFAULT STANDARD CURRENCY) (for uniform number generation).
     */
    public static final int COMPANY_MINIMAL_PROFIT = -5000;
    /**
     * Maximal company additional profit (in DEFAULT STANDARD CURRENCY) (for uniform number generation).
     */
    public static final int COMPANY_MAXIMAL_ADDITIONAL_PROFIT = 15000;
    /**
     * Minimal initial stock value for random company generation (In DEFAULT STANDARD CURRENCY)
     * (for uniform number generation).
     */
    public static final int COMPANY_MINIMAL_INITIAL_STOCK_VALUE = 5;
    /**
     * Maximal additional initial stock value for random company generation (In DEFAULT STANDARD CURRENCY)
     * (for uniform number generation).
     */
    public static final int COMPANY_MAXIMAL_ADDITIONAL_INITIAL_STOCK_VALUE = 15;
    /**
     * Maximal additional number of stocks for random company generation (for uniform number generation).
     */
    public static final int COMPANY_MAXIMAL_ADDITIONAL_STOCK_NUMBER = 20;
    /**
     * Probability of an event of increasing company shares occurring within a single company run cycle.
     */
    public static final double COMPANY_SHARES_INCREASE_PROBABILITY = 0.001;
    /**
     * Mean investor initial funds for random investor generation (in DEFAULT STANDARD CURRENCY)
     * (for normal number generation).
     */
    public static final int INVESTOR_MEAN_INITIAL_FUNDS = 100;
    /**
     * Investor initial funds deviation for random investor generation (in DEFAULT STANDARD CURRENCY)
     * (for normal number generation).
     */
    public static final int INVESTOR_INITIAL_FUNDS_DEVIATION = 20;
    /**
     * Probability of an event of increasing investor funds occurring within a single investor run cycle.
     */
    public static final double INVESTOR_FUNDS_INCREASE_PROBABILITY = 0.001;
    /**
     * Amount of money that investor receives upon investor funds increase event (in DEFAULT STANDARD CURRENCY)
     */
    public static final int INVESTOR_FUNDS_INCREASE_AMOUNT = 25;
    /**
     * Identifier and name of the DEFAULT STANDARD CURRENCY used for trading (that is not equivalent to any other
     * currency in the simulation, if the
     */
    public static final String DEFAULT_CURRENCY = "DEFAULT_STANDARD_CURRENCY";
    /**
     * Seed for RandomService instance (if negative, RandomService instance is set without specified seed)
     */
    public static final int RANDOM_SERVICE_SEED = 0;
    /**
     * Minimal proportion (and maximal when taking the reciprocal) for assets between their current price and initial
     * price for the restoring effect to take place.
     */
    public static final int RESTORING_EFFECT_PROPORTION = 1000;
    /**
     *  Relative change to be applied to the current price (negative when the upper limit is reached, positive when
     *  lower limit is reached, the limits being RESTORING_EFFECT_PROPORTION and 1/RESTORING_EFFECT_PROPORTION).
     */
    public static final double RESTORING_EFFECT_CHANGE = 0.1;
    /**
     * Number of days for which the restoring effect takes place after starting the restoration mechanism on an asset.
     */
    public static final int RESTORING_EFFECT_DURATION = 10;
}
