package simulation.holders;

import simulation.asset.AssetManager;
import simulation.market.Market;
import simulation.util.DebugLogger;
import simulation.util.RandomDataGenerator;

import java.util.HashMap;
import java.util.HashSet;

public abstract class AssetHolder extends Thread {
    HashMap<String, Double> storedAssets;
    HashMap<String, Double> assetsOnSale;
    private HashSet<Market> availableMarkets;
    protected double investmentBudget;
    protected double frozenFunds;
    protected boolean running = false;
    protected boolean log = false;
    protected boolean freezeWithdrawal = false;
    private int id;

    public AssetHolder(int id, double investmentBudget) {
        this.storedAssets = new HashMap<>();
        this.assetsOnSale = new HashMap<>();
        this.availableMarkets = new HashSet<>();
        this.investmentBudget = investmentBudget;
        this.frozenFunds = 0;
        this.id = id;
    }

    public void sendBuyOrder(Market market) {
        var rand = RandomDataGenerator.getInstance();
        String chosenAsset = (String) rand.sampleElement(market.getAvailableAssetTypes().toArray());
        double price = AssetManager.getInstance().getAssetData(chosenAsset).getLatestSellingPrice() * 0.9;
        double amount = this.investmentBudget > price * 2 && rand.yieldRandomNumber(1.0) > 0.7 ? 1.0 : 2.0;
        if (this.investmentBudget < price * amount) {
            return;
        }
        market.addBuyOffer(chosenAsset, this, price, amount);
        this.investmentBudget -= price * amount;
        this.frozenFunds += price * amount;
        if (this.log) {
            DebugLogger.logMessage("[INVESTOR] Sent buy order for " + amount*price);
            DebugLogger.logMessage("Current budget: " + this.investmentBudget);
            DebugLogger.logMessage("Frozen funds: " + this.frozenFunds);
        }
    }

    public void processBuyOrder(String assetType, double price, double amount) {
        double newAmount = this.storedAssets.getOrDefault(assetType, 0.0) + amount;
        this.frozenFunds -= price * amount;
        this.storedAssets.put(assetType, newAmount);
        if (this.log) {
            DebugLogger.logMessage("[INVESTOR] Processed buy order for " + amount * price);
            DebugLogger.logMessage("Current budget: " + this.investmentBudget);
            DebugLogger.logMessage("Frozen funds: " + this.frozenFunds);
        }
    }

    public void processBuyWithdrawal(double price, double amount) {
        this.frozenFunds -= price * amount;
        this.investmentBudget += price * amount;
        if (this.log) {
            DebugLogger.logMessage("[INVESTOR] Process buy withdrawal for " + amount * price);
        }
    }

    public double processBuyOrderAlteration(double price, double amount) {
        double newPrice = price * 1.04;
        if (this.investmentBudget < (newPrice - price) * amount) {
            newPrice = price + this.investmentBudget/amount;
        }
        this.investmentBudget -= (newPrice - price) * amount;
        this.frozenFunds += (newPrice - price) * amount;
        if (this.log) {
            DebugLogger.logMessage("[INVESTOR] Process buy offer alteration to  " + newPrice * amount);
            DebugLogger.logMessage("Current budget: " + this.investmentBudget);
            DebugLogger.logMessage("Frozen funds: " + this.frozenFunds);
        }
        return newPrice;
    }

    public void sendSellOrder(Market market) {
        if (this.storedAssets.isEmpty())
            return;
        var rand = RandomDataGenerator.getInstance();
        String chosenAsset = (String) rand.sampleElement(this.storedAssets.keySet().toArray());
        double availableAmount = this.storedAssets.get(chosenAsset);
        double amount = availableAmount > 2 ? (double) Math.round(0.5 * availableAmount) : availableAmount;
        double price = AssetManager.getInstance().getAssetData(chosenAsset).getLatestSellingPrice() * 1.1;
        market.addSellOffer(chosenAsset, this, price, amount);
        double left = this.storedAssets.get(chosenAsset) - amount;
        if (left > 0)
            this.storedAssets.put(chosenAsset, left);
        else
            this.storedAssets.remove(chosenAsset);
        if (this.log) {
            DebugLogger.logMessage("[INVESTOR] Sent sell offer for " + price * amount);
            DebugLogger.logMessage("Current budget: " + this.investmentBudget);
            DebugLogger.logMessage("Frozen funds: " + this.frozenFunds);
        }
    }

    public void processSellOrder(String assetType, double price, double amount) {
        if (!this.assetsOnSale.containsKey(assetType)) {
            // exception
            return;
        }
        double newAmount = this.assetsOnSale.get(assetType) - amount;
        this.investmentBudget += price * amount;
        if (newAmount > 0)
            this.assetsOnSale.replace(assetType, newAmount);
        else
            this.assetsOnSale.remove(assetType);
        if (this.log) {
            DebugLogger.logMessage("[INVESTOR] Processed sell offer for " + price * amount);
            DebugLogger.logMessage("Current budget: " + this.investmentBudget);
            DebugLogger.logMessage("Frozen funds: " + this.frozenFunds);
        }
    }

    public void processSellWithdrawal(String assetType, double amount) {
        this.storedAssets.put(assetType, this.storedAssets.getOrDefault(assetType, 0.0) + amount);
        if (this.log) {
            DebugLogger.logMessage("[INVESTOR] Processed sell offer withdrawal");
        }
    }

    public double processSellOfferAlteration(double price) {
        if (this.log) {
            DebugLogger.logMessage("[INVESTOR] Processed sell offer alteration");
        }
        return price * 0.96;
    }

    public void generateOrders() {
        var rand = RandomDataGenerator.getInstance();
        var market = (Market) rand.sampleElement(this.availableMarkets.toArray());
        if (market.countSenderOffers(this.id) > 4)
            return;
        if (rand.yieldRandomNumber(1.0) < 0.5)
            this.sendBuyOrder(market);
        if (rand.yieldRandomNumber(1.0) < 0.5)
            this.sendSellOrder(market);
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

    public void enableLogging() {
        this.log = true;
    }

    abstract public void run();
}
