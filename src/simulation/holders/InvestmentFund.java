package simulation.holders;

public class InvestmentFund extends AssetHolder {
    private String name;
    private Investor manager;

    public InvestmentFund(int id, double investmentBudget) {
        super(id, investmentBudget);
    }
}
