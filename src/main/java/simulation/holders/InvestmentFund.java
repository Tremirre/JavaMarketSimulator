package simulation.holders;

import simulation.address.Address;
import simulation.core.SimulationConfig;
import simulation.holders.strategies.InvestmentStrategy;
import simulation.holders.strategies.PassiveCompanyStrategy;
import simulation.market.Market;
import simulation.util.Constants;
import simulation.util.GlobalHoldersLock;
import simulation.util.RandomService;

/**
 * Class representing an investment fund.
 */
public class InvestmentFund extends Company {
    /**
     * Profit made by the investment fund in the current running cycle.
     */
    private double currentCycleProfit;
    /**
     * Revenue made by the investment fund in the current running cycle.
     */
    private double currentCycleRevenue;
    /**
     * Number of cycles between which investment fund pulls out funds.
     */
    private static final int pulloutCycle = 10;

    /**
     * Initializes fund's parameters to provided values.
     * Sets initial profit and revenue to 0.
     * Sets the name of the fund as derivative of the first and second name of the owner.
     * @param id unique entity id.
     * @param numberOfStocks initial number of stocks the fund has of the associated asset in the simulation.
     * @param IPODate date of initial public offering.
     * @param address address of the fund.
     * @param IPOShareValue initial share value.
     * @param strategy investment strategy to be employed
     * @param investmentBudget initial investment budget of the fund.
     * @param ownerName name of the owner.
     * @param ownerSurname second name of the owner.
     */
    public InvestmentFund(int id,
                          int numberOfStocks,
                          String IPODate,
                          Address address,
                          double IPOShareValue,
                          InvestmentStrategy strategy,
                          double investmentBudget,
                          String ownerName,
                          String ownerSurname) {
        super(id, numberOfStocks, ownerName + ' ' + ownerSurname + "'s Investment Fund",
                IPODate, address, IPOShareValue, 0, 0, strategy);
        this.setInvestmentBudget(investmentBudget);
        this.currentCycleProfit = 0;
        this.currentCycleRevenue = 0;
        this.freezeWithdrawal = false;
    }

    /**
     * Pulls out 1% of the fund's budget. (It disappears from the simulation as to simulate the owner taking
     * his earnings).
     */
    private void pulloutFunds() {
        this.investmentBudget *= 0.99;
    }

    /**
     * Resets the cycle profit and revenue and sets the recorded profit and revenue to the current cycle's one.
     */
    @Override
    protected void updateCompanyData() {
        this.profit = this.currentCycleProfit;
        this.revenue = this.currentCycleRevenue;
        this.currentCycleRevenue = 0;
        this.currentCycleProfit = 0;
    }

    /**
     * Sells all stored stocks associated with itself to the provided market.
     * @param market market to which sell offer should be sent.
     */
    @Override
    public void sellAllStocks(Market market) {
        var savedStrategy = this.strategy;
        this.strategy = new PassiveCompanyStrategy(this.strategy.getAssetManager());
        this.sendSellOffer(market);
        this.strategy = savedStrategy;
    }

    /**
     * {@inheritDoc}
     * Money spent on the offer is subtracted from the funds current cycle profit.
     * @param market market to which the offer should be sent.
     */
    @Override
    public void sendBuyOffer(Market market) {
        var diff = -this.investmentBudget;
        super.sendBuyOffer(market);
        diff += this.investmentBudget;
        this.currentCycleProfit -= diff;
    }

    /**
     * {@inheritDoc}
     * Overpay is added to the profit and the revenue of the fund.
     * @param assetType the type of the bought asset.
     * @param overPay overpaid amount of money in DEFAULT STANDARD CURRENCY.
     * @param amount of asset that has been bought.
     */
    @Override
    public void processBuyOffer(String assetType, double overPay, double amount) {
        var diff = -this.investmentBudget;
        super.processBuyOffer(assetType, overPay, amount);
        diff += this.investmentBudget;
        this.currentCycleProfit += diff;
        this.currentCycleRevenue += diff;
    }

    /**
     * {@inheritDoc}
     * Money gained from withdrawal is added to the profit and the revenue of the fund.
     * @param price amount of money to be returned in given currency per 1 unit of asset.
     * @param amount amount of asset that has been offered for buying.
     * @param currency currency of the withdrawn offer.
     */
    @Override
    public void processBuyWithdrawal(double price, double amount, String currency) {
        var diff = -this.investmentBudget;
        super.processBuyWithdrawal(price, amount, currency);
        diff += this.investmentBudget;
        this.currentCycleProfit += diff;
        this.currentCycleRevenue += diff;
    }

    /**
     * {@inheritDoc}
     * Money spent on the offer alteration subtracted from the fund's profit.
     * @param price amount of money originally offered for 1 unit of asset in given currency.
     * @param amount amount of asset that has been offered for buying.
     * @param assetType type of the asset to be bought.
     * @param currency currency of the altered offer.
     * @return altered price per 1 unit of the asset in offer currency.
     */
    @Override
    public double processBuyOfferAlteration(double price, double amount, String assetType, String currency) {
        var diff = -this.investmentBudget;
        var newPrice = super.processBuyOfferAlteration(price, amount, assetType, currency);
        diff += this.investmentBudget;
        this.currentCycleProfit -= diff;
        return newPrice;
    }

    /**
     * {@inheritDoc}
     * Money earned from the sale is added to the profit and revenue of the fund.
     * @param assetType asset to be sold.
     * @param price price per 1 unit of asset in DEFAULT STANDARD CURRENCY.
     * @param amount amount of asset to be sold.
     */
    @Override
    public void processSellOffer(String assetType, double price, double amount) {
        var diff = -this.investmentBudget;
        super.processSellOffer(assetType, price, amount);
        diff += this.investmentBudget;
        this.currentCycleProfit += diff;
        this.currentCycleRevenue += diff;
    }

    /**
     * {@inheritDoc}
     * The price will not be altered if the queried asset is the same as the associated asset of the fund.
     * @param price price of the offer in given currency per 1 unit of asset.
     * @param assetType asset of the offer.
     * @param currency currency of the offer.
     * @return altered price per 1 unit of the asset in offer currency.
     */
    @Override
    public double processSellOfferAlteration(double price, String assetType, String currency) {
        if (this.associatedAsset.equals(assetType))
            return price;
        return super.processSellOfferAlteration(price, assetType, currency);
    }

    /**
     * {@inheritDoc}
     * For investment fund, it freezes withdrawal of assets associated with the fund.
     * @param assetType asset of the offer.
     * @return flag denoting whether fund can withdraw an offer concerning the provided asset.
     */
    @Override
    public boolean canWithdraw(String assetType) {
        if (this.associatedAsset.equals(assetType))
            return false;
        return !this.freezeWithdrawal;
    }

    /**
     * Loops investment fund's while it is allowed to run.
     * The loop is split into two parts - first one at which the fund generates orders and may update its own data,
     * and the second one at which the fund is idle and can be modified from the outside (which is ensured by the lock).
     */
    @Override
    public void run() {
        int cycleNumber = 0;
        while (this.running) {
            GlobalHoldersLock.readLock();
            this.generateOrders();
            if (cycleNumber++ == pulloutCycle) {
                this.pulloutFunds();
                this.updateCompanyData();
            }
            GlobalHoldersLock.readUnlock();
            try {
                Thread.sleep(
                        (long) ((RandomService.getInstance().yieldRandomInteger(
                                Constants.INVESTMENT_FUND_MAXIMAL_ADDITIONAL_SLEEP_TIME
                        ) + Constants.BASE_INVESTMENT_FUND_SLEEP_TIME) * SimulationConfig.getInstance().getTimeMultiplier())
                );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
