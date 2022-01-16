package simulation.holders;

import simulation.core.SimulationConfig;
import simulation.holders.strategies.InvestmentStrategy;
import simulation.util.Constants;
import simulation.util.GlobalHoldersLock;
import simulation.util.RandomService;

public class Investor extends AssetHolder {
    private String firstName;
    private String lastName;

    public Investor(int id, double funds, String firstName, String lastName, InvestmentStrategy strategy) {
        super(id, funds, strategy);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void increaseFunds(double probability) {
        if (RandomService.getInstance().yieldRandomNumber(1) < probability) {
            this.investmentBudget += Constants.INVESTOR_FUNDS_INCREASE_AMOUNT;
        }
    }

    @Override
    public void run() {
        while (this.running) {
            this.increaseFunds(Constants.INVESTOR_FUNDS_INCREASE_PROBABILITY);
            GlobalHoldersLock.readLock();
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
