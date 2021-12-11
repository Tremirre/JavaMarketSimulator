package simulation.holders;

public interface IHolderFactory {
    AssetHolder createHolder(HolderType type);
}
