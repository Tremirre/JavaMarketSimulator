package simulation.market;

import simulation.holders.AssetHolder;
import simulation.offer.BuyOffer;
import simulation.offer.SellOffer;

import java.util.ArrayList;
import java.util.HashSet;

public class CurrenciesMarket extends Market{
    private HashSet<String> currencies;

    @Override
    public void initializeMarket() {

    }

    @Override
    public void addBuyOffer(String assetType, AssetHolder sender, double price, double size) {

    }

    @Override
    public void addSellOffer(String assetType, AssetHolder sender, double price, double size) {

    }

    @Override
    void processOffer(BuyOffer buyOffer, SellOffer sellOffer) {

    }

    @Override
    public void processAllOffers() {

    }

    @Override
    public ArrayList<String> getAvailableAssetTypes() {
        return null;
    }
}
