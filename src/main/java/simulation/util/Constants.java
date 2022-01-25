package simulation.util;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Properties;

/**
 * Class for holding all necessary constants within the application.
 * Settable constants are loaded from staticconfig.properties file.
 */
public final class Constants {
    /**
     * Name of the config file.
     */
    private static final String CONFIG_FILE_NAME = "staticconfig.properties";
    /**
     * Denotes the maximal number of threads that may be spawned by the application.
     */
    public static final int MAX_THREADS;
    /**
     * Base trading time in ms.
     */
    public static final int BASE_TRADING_TIME;
    /**
     * Base investor sleep time in ms.
     */
    public static final int BASE_INVESTOR_SLEEP_TIME;
    /**
     * Investor sleep time deviation in ms (for normal number generation).
     */
    public static final int INVESTOR_SLEEP_TIME_DEVIATION;
    public static final int YEAR = 365;
    /**
     * Company sleep time in ms.
     */
    public static final int COMPANY_SLEEP_TIME;
    /**
     * Base investment fund sleep time in ms.
     */
    public static final int BASE_INVESTMENT_FUND_SLEEP_TIME;
    /**
     * Investment fund max additional sleep time in ms (for uniform number generation).
     */
    public static final int INVESTMENT_FUND_MAXIMAL_ADDITIONAL_SLEEP_TIME;
    /**
     * Investment fund mean initial funds (in DEFAULT STANDARD CURRENCY) (for normal number generation).
     */
    public static final int INVESTMENT_FUND_MEAN_INITIAL_FUNDS;
    /**
     * Investment fund initial funds deviation (in DEFAULT STANDARD CURRENCY) (for normal number generation).
     */
    public static final int INVESTMENT_FUND_INITIAL_FUNDS_DEVIATION;
    /**
     * Constant that serves as a balancing measure when determining the IPO share value and the number of shares
     * Send to the market when creating a random company.
     */
    public static final int COMPANY_GENERATION_CONSTANT;
    /**
     * Minimal company revenue (in DEFAULT STANDARD CURRENCY) (for uniform number generation).
     */
    public static final int COMPANY_MINIMAL_REVENUE;
    /**
     * Maximal company additional revenue (in DEFAULT STANDARD CURRENCY) (for uniform number generation).
     */
    public static final int COMPANY_MAXIMAL_ADDITIONAL_REVENUE;
    /**
     * Minimal company profit (in DEFAULT STANDARD CURRENCY) (for uniform number generation).
     */
    public static final int COMPANY_MINIMAL_PROFIT;
    /**
     * Maximal company additional profit (in DEFAULT STANDARD CURRENCY) (for uniform number generation).
     */
    public static final int COMPANY_MAXIMAL_ADDITIONAL_PROFIT;
    /**
     * Minimal initial stock value for random company generation (In DEFAULT STANDARD CURRENCY)
     * (for uniform number generation).
     */
    public static final int COMPANY_MINIMAL_INITIAL_STOCK_VALUE;
    /**
     * Maximal additional initial stock value for random company generation (In DEFAULT STANDARD CURRENCY)
     * (for uniform number generation).
     */
    public static final int COMPANY_MAXIMAL_ADDITIONAL_INITIAL_STOCK_VALUE;
    /**
     * Maximal additional number of stocks for random company generation (for uniform number generation).
     */
    public static final int COMPANY_MAXIMAL_ADDITIONAL_STOCK_NUMBER;
    /**
     * Probability of an event of increasing company shares occurring within a single company run cycle.
     */
    public static final double COMPANY_SHARES_INCREASE_PROBABILITY;
    /**
     * Mean investor initial funds for random investor generation (in DEFAULT STANDARD CURRENCY)
     * (for normal number generation).
     */
    public static final int INVESTOR_MEAN_INITIAL_FUNDS;
    /**
     * Investor initial funds deviation for random investor generation (in DEFAULT STANDARD CURRENCY)
     * (for normal number generation).
     */
    public static final int INVESTOR_INITIAL_FUNDS_DEVIATION;
    /**
     * Probability of an event of increasing investor funds occurring within a single investor run cycle.
     */
    public static final double INVESTOR_FUNDS_INCREASE_PROBABILITY;
    /**
     * Amount of money that investor receives upon investor funds increase event (in DEFAULT STANDARD CURRENCY)
     */
    public static final int INVESTOR_FUNDS_INCREASE_AMOUNT;
    /**
     * Identifier and name of the DEFAULT STANDARD CURRENCY used for trading (that is not equivalent to any other
     * currency in the simulation, if the
     */
    public static final String DEFAULT_CURRENCY;
    /**
     * Seed for RandomService instance (if negative, RandomService instance is set without specified seed)
     */
    public static final int RANDOM_SERVICE_SEED;
    /**
     * Minimal proportion (and maximal when taking the reciprocal) for assets between their current price and initial
     * price for the restoring effect to take place.
     */
    public static final int RESTORING_EFFECT_PROPORTION;
    /**
     *  Relative change to be applied to the current price (negative when the upper limit is reached, positive when
     *  lower limit is reached, the limits being RESTORING_EFFECT_PROPORTION and 1/RESTORING_EFFECT_PROPORTION).
     */
    public static final double RESTORING_EFFECT_CHANGE;
    /**
     * Number of days for which the restoring effect takes place after starting the restoration mechanism on an asset.
     */
    public static final int RESTORING_EFFECT_DURATION;

    /*
     * Block that reads constants from the properties file.
     */
    static {
        Properties p = new Properties();
        var _MAX_THREADS=2000;
        var _BASE_TRADING_TIME=10;
        var _BASE_INVESTOR_SLEEP_TIME=5;
        var _INVESTOR_SLEEP_TIME_DEVIATION=2;
        var _COMPANY_SLEEP_TIME=50;
        var _BASE_INVESTMENT_FUND_SLEEP_TIME=10;
        var _INVESTMENT_FUND_MAXIMAL_ADDITIONAL_SLEEP_TIME=4;
        var _INVESTMENT_FUND_MEAN_INITIAL_FUNDS=1000;
        var _INVESTMENT_FUND_INITIAL_FUNDS_DEVIATION=100;
        var _COMPANY_GENERATION_CONSTANT=500;
        var _COMPANY_MINIMAL_REVENUE=20000;
        var _COMPANY_MAXIMAL_ADDITIONAL_REVENUE=20000;
        var _COMPANY_MINIMAL_PROFIT=-5000;
        var _COMPANY_MAXIMAL_ADDITIONAL_PROFIT=15000;
        var _COMPANY_MINIMAL_INITIAL_STOCK_VALUE=5;
        var _COMPANY_MAXIMAL_ADDITIONAL_INITIAL_STOCK_VALUE=15;
        var _COMPANY_MAXIMAL_ADDITIONAL_STOCK_NUMBER=20;
        var _COMPANY_SHARES_INCREASE_PROBABILITY=0.001;
        var _INVESTOR_MEAN_INITIAL_FUNDS=100;
        var _INVESTOR_INITIAL_FUNDS_DEVIATION=20;
        var _INVESTOR_FUNDS_INCREASE_PROBABILITY=0.001;
        var _INVESTOR_FUNDS_INCREASE_AMOUNT=25;
        var _DEFAULT_CURRENCY="DEFAULT_STANDARD_CURRENCY";
        var _RANDOM_SERVICE_SEED=0;
        var _RESTORING_EFFECT_PROPORTION=1000;
        var _RESTORING_EFFECT_CHANGE=0.1;
        var _RESTORING_EFFECT_DURATION=10;
        try(var stream = Constants.class.getResourceAsStream(CONFIG_FILE_NAME)) {
            p.load(stream);
            _MAX_THREADS = Integer.parseInt(p.getProperty("MAX_THREADS"));
            _BASE_TRADING_TIME = Integer.parseInt(p.getProperty("BASE_TRADING_TIME"));
            _BASE_INVESTOR_SLEEP_TIME = Integer.parseInt(p.getProperty("BASE_INVESTOR_SLEEP_TIME"));
            _INVESTOR_SLEEP_TIME_DEVIATION = Integer.parseInt(p.getProperty("INVESTOR_SLEEP_TIME_DEVIATION"));
            _COMPANY_SLEEP_TIME = Integer.parseInt(p.getProperty("COMPANY_SLEEP_TIME"));
            _BASE_INVESTMENT_FUND_SLEEP_TIME = Integer.parseInt(p.getProperty("BASE_INVESTMENT_FUND_SLEEP_TIME"));
            _INVESTMENT_FUND_MAXIMAL_ADDITIONAL_SLEEP_TIME = Integer.parseInt(p.getProperty("INVESTMENT_FUND_MAXIMAL_ADDITIONAL_SLEEP_TIME"));
            _INVESTMENT_FUND_MEAN_INITIAL_FUNDS = Integer.parseInt(p.getProperty("INVESTMENT_FUND_MEAN_INITIAL_FUNDS"));
            _INVESTMENT_FUND_INITIAL_FUNDS_DEVIATION = Integer.parseInt(p.getProperty("INVESTMENT_FUND_INITIAL_FUNDS_DEVIATION"));
            _COMPANY_GENERATION_CONSTANT = Integer.parseInt(p.getProperty("COMPANY_GENERATION_CONSTANT"));
            _COMPANY_MINIMAL_REVENUE = Integer.parseInt(p.getProperty("COMPANY_MINIMAL_REVENUE"));
            _COMPANY_MAXIMAL_ADDITIONAL_REVENUE = Integer.parseInt(p.getProperty("COMPANY_MAXIMAL_ADDITIONAL_REVENUE"));
            _COMPANY_MINIMAL_PROFIT = Integer.parseInt(p.getProperty("COMPANY_MINIMAL_PROFIT"));
            _COMPANY_MAXIMAL_ADDITIONAL_PROFIT = Integer.parseInt(p.getProperty("COMPANY_MAXIMAL_ADDITIONAL_PROFIT"));
            _COMPANY_MINIMAL_INITIAL_STOCK_VALUE = Integer.parseInt(p.getProperty("COMPANY_MINIMAL_INITIAL_STOCK_VALUE"));
            _COMPANY_MAXIMAL_ADDITIONAL_INITIAL_STOCK_VALUE = Integer.parseInt(p.getProperty("COMPANY_MAXIMAL_ADDITIONAL_INITIAL_STOCK_VALUE"));
            _COMPANY_MAXIMAL_ADDITIONAL_STOCK_NUMBER = Integer.parseInt(p.getProperty("COMPANY_MAXIMAL_ADDITIONAL_STOCK_NUMBER"));
            _COMPANY_SHARES_INCREASE_PROBABILITY = Double.parseDouble(p.getProperty("COMPANY_SHARES_INCREASE_PROBABILITY"));
            _INVESTOR_MEAN_INITIAL_FUNDS = Integer.parseInt(p.getProperty("INVESTOR_MEAN_INITIAL_FUNDS"));
            _INVESTOR_INITIAL_FUNDS_DEVIATION = Integer.parseInt(p.getProperty("INVESTOR_INITIAL_FUNDS_DEVIATION"));
            _INVESTOR_FUNDS_INCREASE_PROBABILITY = Double.parseDouble(p.getProperty("INVESTOR_FUNDS_INCREASE_PROBABILITY"));
            _INVESTOR_FUNDS_INCREASE_AMOUNT = Integer.parseInt(p.getProperty("INVESTOR_FUNDS_INCREASE_AMOUNT"));
            _DEFAULT_CURRENCY = p.getProperty("DEFAULT_CURRENCY");
            _RANDOM_SERVICE_SEED = Integer.parseInt(p.getProperty("RANDOM_SERVICE_SEED"));
            _RESTORING_EFFECT_PROPORTION = Integer.parseInt(p.getProperty("RESTORING_EFFECT_PROPORTION"));
            _RESTORING_EFFECT_CHANGE = Double.parseDouble(p.getProperty("RESTORING_EFFECT_CHANGE"));
            _RESTORING_EFFECT_DURATION = Integer.parseInt(p.getProperty("RESTORING_EFFECT_DURATION"));
        } catch (IOException e) {
            System.out.println("Failed to load " + CONFIG_FILE_NAME + " file!");
            System.out.println(e.getMessage());
            System.out.println("Constants will be initialized with default values.");
        } catch (NumberFormatException e) {
            System.out.println("Failed to load " + CONFIG_FILE_NAME + " file!");
            System.out.println("Invalid format of one of the values");
            System.out.println(e.getMessage());
            System.out.println("The remaining constants will be initialized with default values.");
        }
        MAX_THREADS=_MAX_THREADS;
        BASE_TRADING_TIME=_BASE_TRADING_TIME;
        BASE_INVESTOR_SLEEP_TIME=_BASE_INVESTOR_SLEEP_TIME;
        INVESTOR_SLEEP_TIME_DEVIATION=_INVESTOR_SLEEP_TIME_DEVIATION;
        COMPANY_SLEEP_TIME=_COMPANY_SLEEP_TIME;
        BASE_INVESTMENT_FUND_SLEEP_TIME=_BASE_INVESTMENT_FUND_SLEEP_TIME;
        INVESTMENT_FUND_MAXIMAL_ADDITIONAL_SLEEP_TIME=_INVESTMENT_FUND_MAXIMAL_ADDITIONAL_SLEEP_TIME;
        INVESTMENT_FUND_MEAN_INITIAL_FUNDS=_INVESTMENT_FUND_MEAN_INITIAL_FUNDS;
        INVESTMENT_FUND_INITIAL_FUNDS_DEVIATION=_INVESTMENT_FUND_INITIAL_FUNDS_DEVIATION;
        COMPANY_GENERATION_CONSTANT=_COMPANY_GENERATION_CONSTANT;
        COMPANY_MINIMAL_REVENUE=_COMPANY_MINIMAL_REVENUE;
        COMPANY_MAXIMAL_ADDITIONAL_REVENUE=_COMPANY_MAXIMAL_ADDITIONAL_REVENUE;
        COMPANY_MINIMAL_PROFIT=_COMPANY_MINIMAL_PROFIT;
        COMPANY_MAXIMAL_ADDITIONAL_PROFIT=_COMPANY_MAXIMAL_ADDITIONAL_PROFIT;
        COMPANY_MINIMAL_INITIAL_STOCK_VALUE=_COMPANY_MINIMAL_INITIAL_STOCK_VALUE;
        COMPANY_MAXIMAL_ADDITIONAL_INITIAL_STOCK_VALUE=_COMPANY_MAXIMAL_ADDITIONAL_INITIAL_STOCK_VALUE;
        COMPANY_MAXIMAL_ADDITIONAL_STOCK_NUMBER=_COMPANY_MAXIMAL_ADDITIONAL_STOCK_NUMBER;
        COMPANY_SHARES_INCREASE_PROBABILITY=_COMPANY_SHARES_INCREASE_PROBABILITY;
        INVESTOR_MEAN_INITIAL_FUNDS=_INVESTOR_MEAN_INITIAL_FUNDS;
        INVESTOR_INITIAL_FUNDS_DEVIATION=_INVESTOR_INITIAL_FUNDS_DEVIATION;
        INVESTOR_FUNDS_INCREASE_PROBABILITY=_INVESTOR_FUNDS_INCREASE_PROBABILITY;
        INVESTOR_FUNDS_INCREASE_AMOUNT=_INVESTOR_FUNDS_INCREASE_AMOUNT;
        DEFAULT_CURRENCY=_DEFAULT_CURRENCY;
        RANDOM_SERVICE_SEED=_RANDOM_SERVICE_SEED;
        RESTORING_EFFECT_PROPORTION=_RESTORING_EFFECT_PROPORTION;
        RESTORING_EFFECT_CHANGE=_RESTORING_EFFECT_CHANGE;
        RESTORING_EFFECT_DURATION=_RESTORING_EFFECT_DURATION;
    }
}
