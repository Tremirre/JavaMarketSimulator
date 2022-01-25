package simulation.holders;

import simulation.core.SimulationConfig;
import simulation.holders.strategies.InvestmentStrategy;
import simulation.util.Constants;
import simulation.util.GlobalHoldersLock;
import simulation.util.RandomService;

/**
 * Class representing an individual investor.
 */
public class Investor extends AssetHolder {
    private String firstName;
    private String lastName;

    /**
     * @param id unique id.
     * @param funds initial investment budget.
     * @param firstName first name.
     * @param lastName last name.
     * @param strategy investment strategy to be employed by the investor.
     */
    public Investor(int id, double funds, String firstName, String lastName, InvestmentStrategy strategy) {
        super(id, funds, strategy);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * With given probability increases investors funds with constant value defined in the constants class
     * @see Constants
     * @param probability probability of fund increase occurring.
     */
    public void increaseFunds(double probability) {
        if (RandomService.getInstance().yieldRandomNumber(1) < probability) {
            this.investmentBudget += Constants.INVESTOR_FUNDS_INCREASE_AMOUNT;
        }
    }

    /**
     * Loops investor's activity, split into two sections - active one when the investor may have his funds increased
     * and generates new orders, and idle one during which investor may be modified from the outside (which is ensured
     * by the lock).
     */
    @Override
    public void run() {
        while (this.running) {
            GlobalHoldersLock.readLock();
            this.increaseFunds(Constants.INVESTOR_FUNDS_INCREASE_PROBABILITY);
            this.generateOrders();
            GlobalHoldersLock.readUnlock();
            try {
                Thread.sleep((long) ((RandomService.getInstance().yieldRandomInteger(Constants.INVESTOR_SLEEP_TIME_DEVIATION) +
                                        Constants.BASE_INVESTOR_SLEEP_TIME) * SimulationConfig.getInstance().getTimeMultiplier()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
