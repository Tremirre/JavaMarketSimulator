package simulation.holders;

import simulation.holders.strategies.InvestmentStrategy;
import simulation.holders.strategies.MomentumInvestmentStrategy;
import simulation.holders.strategies.NaiveInvestmentStrategy;
import simulation.holders.strategies.QualitativeAssessmentStrategy;
import simulation.util.Constants;
import simulation.util.RandomService;
import simulation.util.ResourceHolder;

public class RandomHolderFactory extends HolderFactory {
    private static ResourceHolder resourceHolder;

    public static void setFactoryResource(ResourceHolder resource) {
        resourceHolder = resource;
    }

    @Override
    public Investor createInvestor() {
        if (newThreadExceedsLimit())
            return null;
        var rand = RandomService.getInstance();
        var funds = rand.yieldRandomGaussianNumber(Constants.INVESTOR_INITIAL_FUNDS_DEVIATION,
                Constants.INVESTOR_MEAN_INITIAL_FUNDS);
        InvestmentStrategy strategy;
        switch(rand.yieldRandomInteger(3)) {
            case 0 -> strategy = new NaiveInvestmentStrategy(rand.yieldRandomGaussianNumber(0.03, 0.9));
            case 1 -> strategy = new QualitativeAssessmentStrategy(rand.yieldRandomGaussianNumber(0.05, 1));
            case 2 -> strategy = new MomentumInvestmentStrategy(rand.yieldRandomInteger(5) + 1);
            default -> throw new IllegalStateException("Impossible value returned from random generator");
        }
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
        var country = (String) rand.sampleElement(resourceHolder.getCountries());
        var city = (String) rand.sampleElement(resourceHolder.getCitiesForCountry(country));
        var postalCode = rand.yieldPostCode();
        var streetName = (String) rand.sampleElement(resourceHolder.getStreets());
        var buildingNumber = rand.yieldRandomInteger(1000) + 1;
        var address = new Address(country, city, postalCode, streetName, buildingNumber);
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
        return new Company(id++, initialStockSize, name, date, address, initialStockValue, profit, revenue);
    }

    @Override
    public InvestmentFund createInvestmentFund() {
        if (newThreadExceedsLimit())
            return null;
        return new InvestmentFund(id++,0, new NaiveInvestmentStrategy());
    }
}
