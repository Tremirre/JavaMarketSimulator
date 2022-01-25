package simulation.holders;

import simulation.address.Address;
import simulation.core.SimulationConfig;
import simulation.holders.strategies.InvestmentStrategy;
import simulation.market.Market;
import simulation.util.Constants;
import simulation.util.GlobalHoldersLock;
import simulation.util.RandomService;

import java.util.ArrayList;

/**
 * Class representing the company entity.
 */
public class Company extends AssetHolder {
    /**
     * name of the entity.
     */
    private String name;
    /**
     * Initial Public Offering date of the company's share.
     */
    private final String IPODate;
    /**
     * Address of the company.
     */
    private Address address;
    /**
     * Profit of the company in the previous company running cycle.
     */
    protected double profit;
    /**
     * Revenue of the company in the previous company running cycle.
     */
    protected double revenue;
    /**
     * Asset (Stock) associated with the company.
     */
    protected final String associatedAsset;
    /**
     * List holding sizes of transactions made during each day of the asset associated with the company.
     */
    protected final ArrayList<Integer> dailyTradingVolumes = new ArrayList<>();
    /**
     * List holding history of total prices in DEFAULT STANDARD CURRENCY of the transactions made of the
     * associated asset during each day.
     */
    protected final ArrayList<Double> dailyTotalSales = new ArrayList<>();
    /**
     * Total trading volumes obtained in the current day.
     */
    protected int currentTradingVolume = 0;
    /**
     * Total trading sales obtained in the current day.
     */
    protected double currentTotalSales = 0;
    /**
     * Number of stocks the company has in the simulation.
     */
    protected int numberOfStocks;

    /**
     * Along with the creation of the new company, creates a new stock asset to be associated with that company.
     * Freezes withdrawal (since by default companies should not withdraw offers).
     * Adds initial number of associated asset to the company's stored assets.
     * @param id unique entity id.
     * @param numberOfStocks number of stocks the company has in the simulation.
     * @param name name of the company.
     * @param IPODate Initial Public Offering date.
     * @param address Address of the company.
     * @param IPOShareValue Initial Public Offering value in DEFAULT STANDARD CURRENCY.
     * @param profit Initial profit made of the company
     * @param revenue Initial revenue of the company.
     * @param strategy Investment company taken by the company.
     */
    public Company(int id,
                   int numberOfStocks,
                   String name,
                   String IPODate,
                   Address address,
                   double IPOShareValue,
                   double profit,
                   double revenue,
                   InvestmentStrategy strategy) {
        super(id, 0, strategy);
        this.name = name;
        this.IPODate = IPODate;
        this.address = address;
        this.profit = profit;
        this.revenue = revenue;
        this.numberOfStocks = numberOfStocks;
        StringBuilder stockName = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            if (Character.isLetter(name.charAt(i))) stockName.append(name.charAt(i));
        }
        var stock = this.strategy.getAssetManager().addStockAsset(stockName.toString().toUpperCase(), IPOShareValue, this);
        this.associatedAsset = stock.getUniqueIdentifyingName();
        this.storedAssets.put(this.associatedAsset, (double) numberOfStocks);
        this.freezeWithdrawal = true;
    }

    public void buyout(double percentage) {
        int forSale = (int) percentage * this.numberOfStocks;
        for (var market : this.availableMarkets) {
            if (market.getAvailableAssetTypes().contains(this.associatedAsset)) {
                var price = this.strategy.determineOptimalBuyingPrice(this.associatedAsset);
                market.addSellOffer(this.associatedAsset, this, price, forSale);
                this.numberOfStocks -= forSale;
                return;
            }
        }
    }

    /**
     * Forces company to put all of its shares on sale on the given market.
     * @param market market to which sell offer should be sent.
     */
    public void sellAllStocks(Market market) {
        this.sendSellOffer(market);
    }

    /**
     * Adds money obtained through sale to the revenue and profit of the company.
     * @param assetType asset to be sold.
     * @param price price per 1 unit of asset in DEFAULT STANDARD CURRENCY.
     * @param amount amount of asset to be sold.
     */
    public synchronized void processSellOffer(String assetType, double price, double amount) {
        this.revenue += price * amount;
        this.profit += price * amount;
    }

    public String getCompanyName() {
        return name;
    }

    public void setCompanyName(String name) {
        this.name = name;
    }

    public String getIPODate() {
        return IPODate;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    /**
     * Calculates the capital as a number of stocks associated with the company in the simulation times the latest price
     * of that asset.
     * @return
     */
    public double getCapital() {
        return this.numberOfStocks * this.strategy.getAssetManager()
                .getAssetData(this.associatedAsset)
                .getOpeningPrice();
    }

    /**
     * Records data from the transaction of the asset associated with the company.
     * @param price
     * @param amount
     */
    public void recordTransactionData(double price, double amount) {
        this.currentTradingVolume += amount;
        this.currentTotalSales += price * amount;
    }

    /**
     * Saves total sales and trading volumes to the list and resets the counters.
     */
    public void saveAndResetStockTransactionsData() {
        this.dailyTotalSales.add(this.currentTotalSales);
        this.dailyTradingVolumes.add(this.currentTradingVolume);
        this.currentTotalSales = 0;
        this.currentTradingVolume = 0;
    }

    /**
     * Processes event to be made at the end of the day.
     * For companies, it saves and resets stock transaction data.
     */
    @Override
    public void endDayEvent() {
        this.saveAndResetStockTransactionsData();
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * Randomly changes a company's revenue and profit (ensuring that profit is not greater than revenue).
     */
    protected void updateCompanyData() {
        var rand = RandomService.getInstance();
        this.revenue += rand.yieldRandomGaussianNumber(1000, 0);
        this.profit += rand.yieldRandomGaussianNumber(500, 0);
        this.profit = Math.min(this.revenue, this.profit);
    }

    /**
     * Increases the number of stocks of the company in the simulation by 50% and sends the in an offer to the market.
     */
    protected void increaseNumberOfShares() {
        int newStocks = this.numberOfStocks / 2;
        this.numberOfStocks += newStocks;
        this.storedAssets.put(this.associatedAsset, (double) newStocks);
        for (var market : this.availableMarkets) {
            if (market.getAvailableAssetTypes().contains(this.associatedAsset)) {
                this.sellAllStocks(market);
                return;
            }
        }
    }

    /**
     * Loops the company's activity while until its stopped.
     * It consists of a part at which the company remains dormant (and it can be modified) and of the part
     * that modifies the company data. Both parts are separated by the locks as to ensure no outside modification of
     * company data commences until the company is in dormant state.
     */
    @Override
    public void run() {
        while (this.running) {
            try {
                Thread.sleep((long) (Constants.COMPANY_SLEEP_TIME * SimulationConfig.getInstance().getTimeMultiplier()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            GlobalHoldersLock.readLock();
            this.updateCompanyData();
            if (RandomService.getInstance().yieldRandomNumber(1) < Constants.COMPANY_SHARES_INCREASE_PROBABILITY)
                this.increaseNumberOfShares();
            GlobalHoldersLock.readUnlock();
        }
    }

    public String getAssociatedAsset() {
        return associatedAsset;
    }
}
