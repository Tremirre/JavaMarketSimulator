package simulation.holders;

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

    }

    @Override
    public void run() {

    }
}
