package simulation.holders;

import simulation.asset.AssetManager;
import simulation.holders.strategies.*;
import simulation.util.Constants;
import simulation.util.RandomService;
import simulation.util.ResourceHolder;

public class RandomHolderFactory extends HolderFactory {
    private static ResourceHolder resourceHolder;
    private final AssetManager assetManager;

    public RandomHolderFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public static void setFactoryResource(ResourceHolder resource) {
        resourceHolder = resource;
    }

    private InvestmentStrategy pickRandomStrategy(RandomService rand) {
        switch(rand.yieldRandomInteger(3)) {
            case 0 -> {
                return new NaiveInvestmentStrategy(this.assetManager,
                        rand.yieldRandomGaussianNumber(0.03, 0.9));
            }
            case 1 -> {
                return new QualitativeAssessmentStrategy(this.assetManager,
                        rand.yieldRandomGaussianNumber(0.05, 1));
            }
            case 2 -> {
                return new MomentumInvestmentStrategy(this.assetManager,
                        rand.yieldRandomInteger(5) + 1);
            }
            default -> throw new IllegalStateException("Impossible value returned from random generator");
        }
    }

    @Override
    public Investor createInvestor() {
        if (newThreadExceedsLimit())
            return null;
        var rand = RandomService.getInstance();
        var funds = rand.yieldRandomGaussianNumber(Constants.INVESTOR_INITIAL_FUNDS_DEVIATION,
                Constants.INVESTOR_MEAN_INITIAL_FUNDS);
        InvestmentStrategy strategy = this.pickRandomStrategy(rand);
        var name = (String) rand.sampleElement(resourceHolder.getNames());
        var surname = (String) rand.sampleElement(resourceHolder.getSurnames());
        return new Investor(id++, funds, name, surname, strategy);
    }

    @Override
    public Company createCompany() {
        if (newThreadExceedsLimit())
            return null;
        var rand = RandomService.getInstance();
        var date = rand.yieldDate();
        var address = rand.yieldRandomAddress(resourceHolder);
        var name = (String) rand.sampleElement(resourceHolder.getCompanyNames().toArray());
        resourceHolder.removeCompanyName(name);
        if (name == null) {
            name = rand.yieldRandomString(10);
        } else {
            resourceHolder.removeCompanyName(name);
        }
        var initialStockValue = rand.yieldRandomInteger(Constants.COMPANY_MAXIMAL_ADDITIONAL_INITIAL_STOCK_VALUE)
                + Constants.COMPANY_MINIMAL_INITIAL_STOCK_VALUE;
        var initialStockSize = (Constants.COMPANY_GENERATION_CONSTANT/initialStockValue)
                + rand.yieldRandomInteger(Constants.COMPANY_MAXIMAL_ADDITIONAL_STOCK_NUMBER);
        var profit = rand.yieldRandomInteger(Constants.COMPANY_MAXIMAL_ADDITIONAL_PROFIT) - Constants.COMPANY_MINIMAL_PROFIT;
        var revenue = rand.yieldRandomInteger(Constants.COMPANY_MAXIMAL_ADDITIONAL_REVENUE) + Constants.COMPANY_MINIMAL_REVENUE;
        InvestmentStrategy strategy = new PassiveCompanyStrategy(this.assetManager);
        return new Company(id++, initialStockSize, name, date, address, initialStockValue, profit, revenue, strategy);
    }

    @Override
    public InvestmentFund createInvestmentFund() {
        if (newThreadExceedsLimit())
            return null;
        var rand = RandomService.getInstance();
        var date = rand.yieldDate();
        var address = rand.yieldRandomAddress(resourceHolder);
        var initialStockValue = rand.yieldRandomInteger(Constants.COMPANY_MAXIMAL_ADDITIONAL_INITIAL_STOCK_VALUE)
                + Constants.COMPANY_MINIMAL_INITIAL_STOCK_VALUE;
        var initialStockSize = (Constants.COMPANY_GENERATION_CONSTANT/initialStockValue)
                + rand.yieldRandomInteger(Constants.COMPANY_MAXIMAL_ADDITIONAL_STOCK_NUMBER);
        InvestmentStrategy strategy = this.pickRandomStrategy(rand);
        var ownerName = (String) rand.sampleElement(resourceHolder.getNames());
        var ownerSurname = (String) rand.sampleElement(resourceHolder.getSurnames());
        var initialBudget = rand.yieldRandomGaussianNumber(Constants.INVESTMENT_FUND_INITIAL_FUNDS_DEVIATION,
                Constants.INVESTMENT_FUND_MEAN_INITIAL_FUNDS);
        return new InvestmentFund(id++,
                initialStockSize,
                date,
                address,
                initialStockValue,
                strategy,
                initialBudget,
                ownerName,
                ownerSurname);
    }
}
