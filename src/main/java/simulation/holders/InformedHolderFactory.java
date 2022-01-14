package simulation.holders;

import simulation.address.Address;
import simulation.asset.AssetManager;
import simulation.holders.strategies.InvestmentStrategy;
import simulation.holders.strategies.PassiveCompanyStrategy;

public class InformedHolderFactory extends HolderFactory {

    private String name;
    private String secondName;
    private String IPODate;
    private Address address;
    private double funds;
    private double IPOShareValue;
    private int numberOfStocks;
    private double profit;
    private double revenue;
    private InvestmentStrategy strategy;

    protected InformedHolderFactory(int entitiesCount, AssetManager assetManager) {
        super(entitiesCount, assetManager);
    }

    @Override
    public Investor createInvestor() {
        int id = useID();
        if (id == -1)
            return null;
        return new Investor(id, this.funds, this.name, this.secondName, this.strategy);
    }

    @Override
    public Company createCompany() {
        int id = useID();
        if (id == -1)
            return null;
        return new Company(
                id,
                this.numberOfStocks,
                this.name,
                this.IPODate,
                this.address,
                this.IPOShareValue,
                this.profit,
                this.revenue,
                new PassiveCompanyStrategy(this.assetManager)
        );
    }

    @Override
    public InvestmentFund createInvestmentFund() {
        int id = useID();
        if (id == -1)
            return null;
        return new InvestmentFund(
                id,
                this.numberOfStocks,
                this.IPODate,
                this.address,
                this.IPOShareValue,
                this.strategy,
                this.funds,
                this.name,
                this.secondName
        );
    }

    public void setInvestorData(double funds, String firstName, String secondName, InvestmentStrategy strategy) {
        this.funds = funds;
        this.name = firstName;
        this.secondName = secondName;
        this.strategy = strategy;
    }

    public void setCompanyData(
            int numberOfStocks,
            String name,
            String IPODate,
            Address address,
            double IPOShareValue,
            double profit,
            double revenue
    ) {
        this.numberOfStocks = numberOfStocks;
        this.name = name;
        this.IPODate = IPODate;
        this.address = address;
        this.IPOShareValue = IPOShareValue;
        this.profit = profit;
        this.revenue = revenue;
    }

    public void setInvestmentFundData(
            int numberOfStocks,
            String IPODate,
            Address address,
            double IPOShareValue,
            InvestmentStrategy strategy,
            double funds,
            String managerName,
            String managerSecondName
    ) {
        this.numberOfStocks = numberOfStocks;
        this.IPODate = IPODate;
        this.address = address;
        this.IPOShareValue = IPOShareValue;
        this.strategy = strategy;
        this.funds = funds;
        this.name = managerName;
        this.secondName = managerSecondName;
    }
}
