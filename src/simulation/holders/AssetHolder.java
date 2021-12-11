package simulation.holders;

import simulation.market.Market;

import java.util.HashMap;

public abstract class AssetHolder extends Thread {
    HashMap<String, Double> storedAssets;
    HashMap<String, Double> assetsOnSale;

    private double investmentBudget;
    private double frozenFunds;
    private int id;

    public void sendSellOrder(Market market) {
        if (this.storedAssets.isEmpty()) {
            return;
        }
    }

    public void sendBuyOrder(Market market) {

    }

    public void processBuyOrder(Market market) {

    }

    public void processSellOrder(Market market) {

    }

    public void generateOrders(Market market) {

    }

    public void run(){

    }
}
