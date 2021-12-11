package simulation.holders;

public class RandomHolderFactory implements IHolderFactory{
    private static int id = 0;

    @Override
    public AssetHolder createHolder(HolderType type) {
        switch (type) {
            case INVESTOR -> {

            }
            case INVESTMENT_FUND -> {

            }
            case COMPANY -> {

            }
            default -> {
                //raise exception
            }
        }
        return null;
    }
}
