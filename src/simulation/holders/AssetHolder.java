package simulation.holders;

import simulation.asset.AssetManager;
import simulation.market.Market;
import simulation.util.RandomService;

import java.util.HashMap;
import java.util.HashSet;

public abstract class AssetHolder extends Thread {
    HashMap<String, Double> storedAssets;
    HashMap<String, Double> assetsOnSale;
    private HashSet<Market> availableMarkets;
    protected double investmentBudget;
    protected boolean running = false;
    protected boolean freezeWithdrawal = false;
    final private int id;

    public AssetHolder(int id, double investmentBudget) {
        this.storedAssets = new HashMap<>();
        this.assetsOnSale = new HashMap<>();
        this.availableMarkets = new HashSet<>();
        this.investmentBudget = investmentBudget;
        this.id = id;
    }

    public void sendBuyOffer(Market market) {
        if (this.investmentBudget < 1)
            return;
        var rand = RandomService.getInstance();
        String chosenAsset = (String) rand.sampleElement(market.getAvailableAssetTypes().toArray());
        double price = AssetManager.getInstance().getAssetData(chosenAsset).getLatestAverageSellingPrice() * 0.9;
        double amount;
        if (!AssetManager.getInstance().getAssetData(chosenAsset).isSplittable()) {
            amount = rand.yieldRandomInteger(5);
            while (this.investmentBudget < price * amount && amount > 0) {
                amount--;
            }
        } else {
            amount = rand.yieldRandomNumber(3.5) + 0.5;
            while (this.investmentBudget < price * amount && amount > 0.1) {
                amount/=2;
            }
        }
        if (amount < 0.25) {
            return;
        }
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

    public double processBuyOfferAlteration(double price, double amount) {
        double newPrice = price * 1.04;
        if (this.investmentBudget < (newPrice - price) * amount) {
            newPrice = price + this.investmentBudget/amount;
        }
        this.investmentBudget -= (newPrice - price) * amount;
        return newPrice;
    }

    public void sendSellOffer(Market market) {
        var availableAssets = this.storedAssets.keySet();
        availableAssets.retainAll(market.getAvailableAssetTypes());
        if (availableAssets.isEmpty())
            return;
        var rand = RandomService.getInstance();
        String chosenAsset = (String) rand.sampleElement(availableAssets.toArray());
        double availableAmount = this.storedAssets.get(chosenAsset);
        double amount = availableAmount > 2 ? (double) Math.round(0.5 * availableAmount) : availableAmount;
        double price = AssetManager.getInstance().getAssetData(chosenAsset).getLatestAverageSellingPrice() * 1.1;
        double left = (this.storedAssets.get(chosenAsset) - amount);
        if (left > 0)
            this.storedAssets.put(chosenAsset, left);
        else
            this.storedAssets.remove(chosenAsset);
        this.assetsOnSale.put(chosenAsset, this.assetsOnSale.getOrDefault(chosenAsset, 0.0) + amount);
        market.addSellOffer(chosenAsset, this, price, amount);
    }

    public void processSellOffer(String assetType, double price, double amount) {
        if (!this.assetsOnSale.containsKey(assetType))
            throw new RuntimeException("Invalid asset type in selling order processing: " + assetType);
        double newAmount = this.assetsOnSale.get(assetType) - amount;
        this.investmentBudget += price * amount;
        if (newAmount > 0)
            this.assetsOnSale.replace(assetType, newAmount);
        else
            this.assetsOnSale.remove(assetType);
    }

    public void processSellWithdrawal(String assetType, double amount) {
        this.storedAssets.put(assetType, this.storedAssets.getOrDefault(assetType, 0.0) + amount);
    }

    public double processSellOfferAlteration(double price) {
        return price * 0.96;
    }

    public void generateOrders() {
        var rand = RandomService.getInstance();
        var market = (Market) rand.sampleElement(this.availableMarkets.toArray());
        if (market.countSenderOffers(this.id) > 4)
            return;
        if (rand.yieldRandomNumber(1.0) < 0.5)
            this.sendBuyOffer(market);
        if (rand.yieldRandomNumber(1.0) < 0.5)
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
