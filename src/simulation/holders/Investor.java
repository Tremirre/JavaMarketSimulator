package simulation.holders;

import simulation.util.RandomDataGenerator;

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
        if (RandomDataGenerator.getInstance().yieldRandomNumber(1) < probability) {
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
            this.generateOrders();
            try {
                Thread.sleep(RandomDataGenerator.getInstance().yieldRandomInteger(30) + 20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
