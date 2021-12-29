package simulation.holders;

import simulation.util.Constants;
import simulation.util.GlobalMarketLock;
import simulation.util.RandomService;

public class Investor extends AssetHolder {
    private String firstName;
    private String lastName;
    private double investmentInertia;

    public Investor(int id, double funds, String firstName, String lastName, double investmentInertia) {
        super(id, funds);
        this.firstName = firstName;
        this.lastName = lastName;
        this.investmentInertia = investmentInertia;
    }

    public void increaseFunds(double probability){
        if (RandomService.getInstance().yieldRandomNumber(1) < probability) {
            this.investmentBudget += 50;
        }
    }

    @Override
    public void print() {
        System.out.println("Investor " + this.firstName + ' ' + this.lastName);
        System.out.println("His current funds: " + this.investmentBudget);
    }

    @Override
    public void run() {
        while (this.running) {
            this.increaseFunds(0.05);
            GlobalMarketLock.readLock();
            this.generateOrders();
            GlobalMarketLock.readUnlock();
            try {
                Thread.sleep(RandomService.getInstance().yieldRandomInteger(Constants.INVESTOR_SLEEP_TIME_DEVIATION) +
                        Constants.BASE_INVESTOR_SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
