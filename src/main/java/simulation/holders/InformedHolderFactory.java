package simulation.holders;

import simulation.address.Address;
import simulation.asset.AssetManager;
import simulation.holders.strategies.InvestmentStrategy;
import simulation.holders.strategies.PassiveCompanyStrategy;

/**
 * This class is tasked with creating new trading entities with given parameters.
 */
public class InformedHolderFactory extends HolderFactory {

    /**
     * Name of the company / first name of the investor / name of the investment fund.
     */
    private String name;
    /**
     * Second name of the investor.
     */
    private String secondName;
    /**
     * IPO date of the company / investment fund.
     */
    private String IPODate;
    /**
     * Address of the company / investment fund.
     */
    private Address address;
    /**
     * Initial investment budget of the investor / investment fund.
     */
    private double funds;
    /**
     * IPO share value of the company / investment fund.
     */
    private double IPOShareValue;
    /**
     * Initial number of stocks of the company / investment fund.
     */
    private int numberOfStocks;
    /**
     * Initial profit of the company / investment fund.
     */
    private double profit;
    /**
     * Initial revenue of the company / investment fund.
     */
    private double revenue;
    /**
     * Investment strategy of the investor / investment fund.
     */
    private InvestmentStrategy strategy;

    /**
     * Initializes fields with provided values and objects.
     * @param entitiesCount number of dynamic entities (threads) in the simulation.
     * @param assetManager reference to the asset manager.
     */
    public InformedHolderFactory(int entitiesCount, AssetManager assetManager) {
        super(entitiesCount, assetManager);
    }

    /**
     * Creates new investor according to the previously provided data.
     * @return new Investor.
     */
    @Override
    public Investor createInvestor() {
        int id = useID();
        if (id == -1)
            return null;
        return new Investor(id, this.funds, this.name, this.secondName, this.strategy);
    }

    /**
     * Creates new company according to the previously provided data.
     * @return new Company.
     */
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

    /**
     * Creates new investment fund according to the previously provided data.
     * @return new Investment Fund.
     */
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

    /**
     * Sets data for creating new investor.
     * @param funds initial funds of the investor.
     * @param firstName first name of the investor.
     * @param secondName second name of the investor.
     * @param strategy Investment Strategy of the investor.
     */
    public void setInvestorData(double funds, String firstName, String secondName, InvestmentStrategy strategy) {
        this.funds = funds;
        this.name = firstName;
        this.secondName = secondName;
        this.strategy = strategy;
    }

    /**
     * Sets data for creating new Company.
     * @param numberOfStocks initial number of stocks of the company.
     * @param name name of the company.
     * @param IPODate IPO date of the company.
     * @param address address of the company.
     * @param IPOShareValue initial share value of the company.
     * @param profit company initial profit.
     * @param revenue company initial revenue.
     */
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

    /**
     * Sets data for creating new Investment Fund.
     * @param numberOfStocks initial number of stocks of the fund.
     * @param IPODate IPO date of the investment fund.
     * @param address address of the investment fund.
     * @param IPOShareValue initial share value of the investment fund.
     * @param strategy investment fund strategy.
     * @param funds initial funds of the investment fund.
     * @param managerName first name of the manager of the fund.
     * @param managerSecondName second name of the manager of the fund.
     */
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
