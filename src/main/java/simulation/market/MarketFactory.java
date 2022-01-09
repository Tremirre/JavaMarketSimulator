package simulation.market;

public interface MarketFactory {
    Market createMarket(MarketType type);
}
