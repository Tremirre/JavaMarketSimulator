package simulation.offer;

import simulation.asset.AssetManager;

public class BuyOffer extends Offer {
    protected BuyingEntity sender;

    BuyOffer(int id, String assetType, BuyingEntity sender, double price, double size, String currency) {
        super(id, assetType, price, size, currency);
        this.sender = sender;
    }

    @Override
    public void updatePrice() {
        double latestOfferCurrencyRate = AssetManager.getInstance().findPrice(this.offerCurrency);
        this.price = this.sender.processBuyOfferAlteration(this.price*latestOfferCurrencyRate, this.size, this.assetType);
        this.price/=latestOfferCurrencyRate;
    }

    @Override
    public void withdraw() {
        double latestOfferCurrencyRate = AssetManager.getInstance().findPrice(this.offerCurrency);
        this.sender.processBuyWithdrawal(this.price * latestOfferCurrencyRate, this.size);
    }

    public BuyingEntity getSender() {return this.sender;}
}
