package simulation.holders;

import simulation.util.Constants;
import simulation.util.RandomService;

public class RandomHolderFactory extends HolderFactory {
    @Override
    public Investor createInvestor() {
        if (newThreadExceedsLimit())
            return null;
        var rand = RandomService.getInstance();
        var funds = RandomService.getInstance().yieldRandomGaussianNumber(20, 100);
        return new Investor(id++, funds, rand.yieldName(), rand.yieldSurname(), 0.9);
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
        return new Company(id++, initialStockSize, name, date, address, initialStockValue, 0 ,0, 0,0);
    }

    @Override
    public InvestmentFund createInvestmentFund() {
        if (newThreadExceedsLimit())
            return null;
        return new InvestmentFund(id++, 0);
    }
}
