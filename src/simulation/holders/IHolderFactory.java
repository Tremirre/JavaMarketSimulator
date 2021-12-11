package simulation.holders;

import java.io.IOException;

public interface IHolderFactory {
    AssetHolder createHolder(HolderType type) throws IOException;
}
