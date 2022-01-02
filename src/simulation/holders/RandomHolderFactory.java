package simulation.holders;

import simulation.holders.strategies.InvestmentStrategy;
import simulation.holders.strategies.MomentumInvestmentStrategy;
import simulation.holders.strategies.NaiveInvestmentStrategy;
import simulation.holders.strategies.QualitativeAssessmentStrategy;
import simulation.util.Constants;
import simulation.util.RandomService;

public class RandomHolderFactory extends HolderFactory {
    @Override
    public Investor createInvestor() {
        if (newThreadExceedsLimit())
            return null;
        var rand = RandomService.getInstance();
        var funds = RandomService.getInstance().yieldRandomGaussianNumber(20, 100);
        InvestmentStrategy strategy;
        switch(rand.yieldRandomInteger(3)) {
            case 0 -> strategy = new NaiveInvestmentStrategy(rand.yieldRandomGaussianNumber(0.03, 0.9));
            case 1 -> strategy = new QualitativeAssessmentStrategy(rand.yieldRandomGaussianNumber(0.05, 1));
            case 2 -> strategy = new MomentumInvestmentStrategy(rand.yieldRandomInteger(5) + 1);
            default -> throw new IllegalStateException("Impossible value returned from random generator");
        }
        return new Investor(id++, funds, rand.yieldName(), rand.yieldSurname(), strategy);
    }

    @Override
    public Company createCompany() {
        if (newThreadExceedsLimit())
            return null;
        var rand = RandomService.getInstance();
        var date = rand.yieldDate();
        var address = Address.getRandomAddress();
        var name = rand.useCompanyName();
        name = name == null ? rand.yieldRandomString(10) : name;
        var initialStockValue = rand.yieldRandomInteger(15) + 5;
        var initialStockSize = (Constants.COMPANY_GENERATION_CONSTANT/initialStockValue) + rand.yieldRandomInteger(20);
        var profit = rand.yieldRandomInteger(Constants.COMPANY_MAXIMAL_ADDITIONAL_PROFIT) - Constants.COMPANY_MINIMAL_PROFIT;
        var revenue = rand.yieldRandomInteger(Constants.COMPANY_MAXIMAL_ADDITIONAL_REVENUE) + Constants.COMPANY_MINIMAL_REVENUE;
        return new Company(id++, initialStockSize, name, date, address, initialStockValue, profit, revenue);
    }

    @Override
    public InvestmentFund createInvestmentFund() {
        if (newThreadExceedsLimit())
            return null;
        return new InvestmentFund(id++,0, new NaiveInvestmentStrategy());
    }
}
