package simulation.holders;

import simulation.core.SimulationConfig;
import simulation.holders.strategies.InvestmentStrategy;
import simulation.market.Market;
import simulation.offer.BuyingEntity;
import simulation.offer.SellingEntity;
import simulation.util.RandomService;

import java.util.HashMap;
import java.util.HashSet;

/**
 * This class holds common functionality between all trading entities.
 * It and all its children are threads that may send both sell and buy orders and change dynamically as time passes.
 */
public abstract class AssetHolder extends Thread implements SellingEntity, BuyingEntity {
    /**
     * Assets stored by the entity.
     */
    protected HashMap<String, Double> storedAssets;
    /**
     * Set of markets, the entity can trade on.
     */
    protected HashSet<Market> availableMarkets;
    protected double investmentBudget;
    protected boolean running = false;
    protected boolean freezeWithdrawal = false;
    /**
     * Investment Strategy of the entity.
     */
    protected InvestmentStrategy strategy;
    /**
     * Unique identifier.
     */
    final private int id;

    /**
     * Constructor initializing containers and filling fields with provided parameters.
     * @param id unique identifier.
     * @param investmentBudget Budget available to the entity for trading.
     * @param strategy Strategy employed by the entity.
     */
    public AssetHolder(int id, double investmentBudget, InvestmentStrategy strategy) {
        this.storedAssets = new HashMap<>();
        this.availableMarkets = new HashSet<>();
        this.investmentBudget = investmentBudget;
        this.id = id;
        this.strategy = strategy;
    }

    /**
     * Sends a buy offer to the provided market with parameters obtained through the entity's investment strategy.
     * Money added to the offer is subtracted from the investment budget.
     * Sending offer will not commence if the entity has very low budget, or if its investment strategy returned 0 as
     * recommended amount (denoting that the buy offer should not be sent).
     * @param market market to which the offer should be sent.
     */
    protected void sendBuyOffer(Market market) {
        if (this.investmentBudget < 1)
            return;
        String chosenAsset = this.strategy.chooseAssetToBuy(market.getAvailableAssetTypes());
        if (chosenAsset == null) {
            return;
        }
        double price = this.strategy.determineOptimalBuyingPrice(chosenAsset);

        String assetCurrency = market.getAssetTradingCurrency(chosenAsset);
        double convertedPrice = price / this.strategy.getAssetManager().findPrice(assetCurrency);

        double amount = this.strategy.determineOptimalBuyingSize(chosenAsset, price, this.investmentBudget);
        if (amount <= 0)
            return;
        this.investmentBudget -= price * amount;
        market.addBuyOffer(chosenAsset, this, convertedPrice, amount);
    }

    /**
     * Adds overpaid amount of money to the investment budget, extends the stored assets by the new asset.
     * @param assetType the type of the bought asset.
     * @param overPay overpaid amount of money in DEFAULT STANDARD CURRENCY.
     * @param amount of asset that has been bought.
     */
    @Override
    public void processBuyOffer(String assetType, double overPay, double amount) {
        this.investmentBudget += overPay;
        double newAmount = this.storedAssets.getOrDefault(assetType, 0.0) + amount;
        this.storedAssets.put(assetType, newAmount);
    }

    /**
     * Adds the money invested into the offer back to the investment budget after conversion.
     * @param price amount of money to be returned in given currency per 1 unit of asset.
     * @param amount amount of asset that has been offered for buying.
     * @param currency currency of the withdrawn offer.
     */
    @Override
    public void processBuyWithdrawal(double price, double amount, String currency) {
        var rate = this.strategy.getAssetManager().findPrice(currency);
        this.investmentBudget += price * rate * amount;
    }

    /**
     * Alters the price of the buy offer according to the Investment Strategy and the size of the investment budget.
     * @param price amount of money originally offered for 1 unit of asset in given currency.
     * @param amount amount of asset that has been offered for buying.
     * @param assetType type of the asset to be bought.
     * @param currency currency of the altered offer.
     * @return new price converted to the offer currency.
     */
    @Override
    public double processBuyOfferAlteration(double price, double amount, String assetType, String currency) {
        var rate = this.strategy.getAssetManager().findPrice(currency);
        var convertedPrice = rate * price;
        double newPrice = this.strategy.updateBuyPrice(convertedPrice, amount, this.investmentBudget, assetType);
        this.investmentBudget -= (newPrice - convertedPrice) * amount;
        return newPrice / rate;
    }

    /**
     * Sends a sell offer of an owned asset that can be traded on the given market,
     * with parameters obtained through the entity's investment strategy.
     * Amount of asset offered for sale is subtracted from the entity's stored assets.
     * Sending offer will not commence if the entity does not have any assets that can be traded on the market.
     * @param market market to which the offer should be sent.
     */
    protected void sendSellOffer(Market market) {
        var availableAssets = new HashSet<>(this.storedAssets.keySet());
        availableAssets.retainAll(market.getAvailableAssetTypes());
        if (availableAssets.isEmpty())
            return;
        String chosenAsset = this.strategy.chooseAssetToSell(availableAssets);
        if (chosenAsset == null) {
            return;
        }
        double amount = this.strategy.determineOptimalSellingSize(chosenAsset, this.storedAssets.get(chosenAsset));
        double price = this.strategy.determineOptimalSellingPrice(chosenAsset);
        String assetCurrency = market.getAssetTradingCurrency(chosenAsset);
        double convertedPrice = price / this.strategy.getAssetManager().findPrice(assetCurrency);

        double left = (this.storedAssets.get(chosenAsset) - amount);
        if (left > 0)
            this.storedAssets.put(chosenAsset, left);
        else
            this.storedAssets.remove(chosenAsset);
        market.addSellOffer(chosenAsset, this, convertedPrice, amount);
    }

    /**
     * Adds to the budget money gained from the sale.
     * @param assetType asset to be sold.
     * @param price price per 1 unit of asset in DEFAULT STANDARD CURRENCY.
     * @param amount amount of asset to be sold.
     */
    @Override
    public void processSellOffer(String assetType, double price, double amount) {
        this.investmentBudget += price * amount;
    }

    /**
     * Returns previously offered asset for sale back to the entity's stored assets (in the same amount).
     * @param assetType asset of the offer to be withdrawn.
     * @param amount amount of the asset of the withdrawn offer.
     */
    @Override
    public void processSellWithdrawal(String assetType, double amount) {
        this.storedAssets.put(assetType, this.storedAssets.getOrDefault(assetType, 0.0) + amount);
    }

    /**
     * Alters the sell offer's price according to the investment strategy.
     * @param price price of the offer in given currency per 1 unit of asset.
     * @param assetType asset of the offer.
     * @param currency currency of the offer.
     * @return new price in the currency of the offer.
     */
    @Override
    public double processSellOfferAlteration(double price, String assetType, String currency) {
        var rate = this.strategy.getAssetManager().findPrice(currency);
        return this.strategy.updateSellPrice(price * rate, assetType) / rate;
    }

    /**
     * Generates new orders picking a random market from the available ones and randomly sends a buy or sell offer
     * according to the probabilities set in the SimulationConfig (as proportion of bulls/bears).
     * @see SimulationConfig
     */
    protected void generateOrders() {
        var rand = RandomService.getInstance();
        var market = (Market) rand.sampleElement(this.availableMarkets.toArray());
        if (market == null)
            return;
        if (rand.yieldRandomNumber(1) < SimulationConfig.getInstance().getBullProportion())
            this.sendBuyOffer(market);
        if (rand.yieldRandomNumber(1) < SimulationConfig.getInstance().getBearProportion())
            this.sendSellOffer(market);
    }

    /**
     * Gives access to the set of markets to the entity for trading.
     * @param markets set of markets to which entity is to gain access.
     */
    public void giveAccessToMarkets(HashSet<Market> markets) {
        this.availableMarkets = markets;
    }

    public double getInvestmentBudget() {
        return this.investmentBudget;
    }

    public InvestmentStrategy getStrategy() {
        return this.strategy;
    }

    public void setStrategy(InvestmentStrategy strategy) {
        this.strategy = strategy;
    }

    public HashMap<String, Double> getOwnedAssets() {
        return this.storedAssets;
    }

    @Override
    public int getID() {
        return this.id;
    }

    /**
     * Stops the entity from running (i.e. shut-downs the thread)
     */
    public void stopRunning() {
        this.running = false;
    }

    /**
     * @param assetType asset of the offer.
     * @return flag denoting whether the entity may or may not withdraw offers.
     */
    @Override
    public boolean canWithdraw(String assetType) {
        return !this.freezeWithdrawal;
    }

    /**
     * @return flag denoting whether the entity is running.
     */
    public boolean isRunning() {
        return this.running;
    }

    public void setInvestmentBudget(double budget) {
        this.investmentBudget = budget;
    }

    /**
     * Processes entities end day event (nothing by default).
     */
    public void endDayEvent() {}

    /**
     * Starts the thread.
     */
    @Override
    public void start() {
        this.running = true;
        super.start();
    }
}
