package simulation.holders;

import simulation.holders.strategies.InvestmentStrategy;

public class InvestmentFund extends AssetHolder {
    private String name;
    private Investor manager;

    public InvestmentFund(int id, double investmentBudget, InvestmentStrategy strategy) {
        super(id, investmentBudget, strategy);
    }

    @Override
    public void print() {

    }

    @Override
    public void run() {

    }
}
