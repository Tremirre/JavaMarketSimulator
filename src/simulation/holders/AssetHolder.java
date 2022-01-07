package simulation.holders;

import simulation.SimulationConfig;
import simulation.asset.AssetManager;
import simulation.holders.strategies.InvestmentStrategy;
import simulation.market.Market;
import simulation.offer.BuyingEntity;
import simulation.offer.SellingEntity;
import simulation.util.RandomService;

import java.util.HashMap;
import java.util.HashSet;

public abstract class AssetHolder extends Thread implements SellingEntity, BuyingEntity {
    protected HashMap<String, Double> storedAssets;
    private  HashSet<Market> availableMarkets;
    protected double investmentBudget;
    protected boolean running = false;
    protected boolean freezeWithdrawal = false;
    protected InvestmentStrategy strategy;
    final private int id;

    public AssetHolder(int id, double investmentBudget, InvestmentStrategy strategy) {
        this.storedAssets = new HashMap<>();
        this.availableMarkets = new HashSet<>();
        this.investmentBudget = investmentBudget;
        this.id = id;
        this.strategy = strategy;
    }

    protected void sendBuyOffer(Market market) {
        if (this.investmentBudget < 1)
            return;
        String chosenAsset = this.strategy.chooseAssetToBuy(market.getAvailableAssetTypes());
        if (chosenAsset == null) {
            return;
        }
        double price = this.strategy.determineOptimalBuyingPrice(chosenAsset);

        // Buying the currency in necessary amount according to the latest price
        String assetCurrency = market.getAssetTradingCurrency(chosenAsset);
        double convertedPrice = price * AssetManager.getInstance().findPrice(assetCurrency);

        double amount = this.strategy.determineOptimalBuyingSize(chosenAsset, price, this.investmentBudget);
        if (amount <= 0)
            return;
        this.investmentBudget -= price * amount;
        market.addBuyOffer(chosenAsset, this, convertedPrice, amount, assetCurrency);
    }

    public void processBuyOffer(String assetType, double overPay, double amount) {
        this.investmentBudget += overPay;
        double newAmount = this.storedAssets.getOrDefault(assetType, 0.0) + amount;
        this.storedAssets.put(assetType, newAmount);
    }

    public void processBuyWithdrawal(double price, double amount) {
        this.investmentBudget += price * amount;
    }

    public double processBuyOfferAlteration(double price, double amount, String assetType) {
        double newPrice = this.strategy.updateBuyPrice(price, amount, this.investmentBudget, assetType);
        this.investmentBudget -= (newPrice - price) * amount;
        return newPrice;
    }

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
        double convertedPrice = price * AssetManager.getInstance().findPrice(assetCurrency);

        double left = (this.storedAssets.get(chosenAsset) - amount);
        if (left > 0)
            this.storedAssets.put(chosenAsset, left);
        else
            this.storedAssets.remove(chosenAsset);
        market.addSellOffer(chosenAsset, this, convertedPrice, amount);
    }

    public void processSellOffer(String assetType, double price, double amount) {
        this.investmentBudget += price * amount;
    }

    public void processSellWithdrawal(String assetType, double amount) {
        this.storedAssets.put(assetType, this.storedAssets.getOrDefault(assetType, 0.0) + amount);
    }

    public double processSellOfferAlteration(double price, String assetType) {
        return this.strategy.updateSellPrice(price, assetType);
    }

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

    public void giveAccessToMarkets(HashSet<Market> markets) {
        this.availableMarkets = markets;
    }

    public double getInvestmentBudget() {
        return this.investmentBudget;
    }

    public int getID() {
        return this.id;
    }

    public void stopRunning() {
        this.running = false;
    }

    public boolean canWithdraw() {
        return !this.freezeWithdrawal;
    }

    public boolean isRunning() {
        return this.running;
    }

    public void setInvestmentBudget(double budget) {
        this.investmentBudget = budget;
    }

    public void start() {
        this.running = true;
        super.start();
    }
}
