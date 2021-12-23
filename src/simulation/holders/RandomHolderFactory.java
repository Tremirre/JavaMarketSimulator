package simulation.holders;

import simulation.util.Constants;
import simulation.util.RandomDataGenerator;

public class RandomHolderFactory extends HolderFactory {
    @Override
    public Investor createInvestor() {
        if (newThreadExceedsLimit())
            return null;
        var rand = RandomDataGenerator.getInstance();
        var funds = RandomDataGenerator.getInstance().yieldRandomGaussianNumber(20, 100);
        return new Investor(id++, funds, rand.yieldName(), rand.yieldSurname(), 0.9);
    }

    @Override
    public Company createCompany() {
        if (newThreadExceedsLimit())
            return null;
        var rand = RandomDataGenerator.getInstance();
        var date = rand.yieldDate();
        var address = Address.getRandomAddress();
        var name = rand.useCompanyName();
        return new Company(id++, 10, name, date, address, 10, 0 ,0, 0,0);
    }

    @Override
    public InvestmentFund createInvestmentFund() {
        if (newThreadExceedsLimit())
            return null;
        return new InvestmentFund(id++, 0);
    }
}
