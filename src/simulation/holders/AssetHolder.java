package simulation.holders;

import simulation.SimulationConfig;
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
    private final InvestmentStrategy strategy;
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
        double price = this.strategy.determineOptimalBuyingPrice(chosenAsset);
        double amount = this.strategy.determineOptimalBuyingSize(chosenAsset, price, this.investmentBudget);
        if (amount <= 0)
            return;
        this.investmentBudget -= price * amount;
        market.addBuyOffer(chosenAsset, this, price, amount);
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
        String chosenAsset = this.strategy.chooseAssetToSell(this.storedAssets.keySet());
        double amount = this.strategy.determineOptimalSellingSize(chosenAsset, this.storedAssets.get(chosenAsset));
        double price = this.strategy.determineOptimalSellingPrice(chosenAsset);
        double left = (this.storedAssets.get(chosenAsset) - amount);
        if (left > 0)
            this.storedAssets.put(chosenAsset, left);
        else
            this.storedAssets.remove(chosenAsset);
        market.addSellOffer(chosenAsset, this, price, amount);
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
        if (rand.yieldRandomNumber(1) < SimulationConfig.getInstance().getBullProportion())
            this.sendBuyOffer(market);
        if (rand.yieldRandomNumber(1) < SimulationConfig.getInstance().getBearProportion())
            this.sendSellOffer(market);
    }

    public void giveAccessToMarkets(HashSet<Market> markets) {
        this.availableMarkets = markets;
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

    public void start() {
        this.running = true;
        super.start();
    }

    abstract public void print();
}
