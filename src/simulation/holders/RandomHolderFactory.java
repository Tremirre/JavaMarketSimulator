package simulation.holders;

import simulation.util.RandomDataGenerator;

import java.io.IOException;

public class RandomHolderFactory extends HolderFactory{
    @Override
    public AssetHolder createHolder(HolderType type) throws IOException {
        switch (type) {
            case INVESTOR -> {
                var rand = RandomDataGenerator.getInstance();
                var funds = RandomDataGenerator.getInstance().yieldRandomGaussianNumber(20, 100);
                return new Investor(HolderFactory.fetchID(), funds, rand.yieldName(), rand.yieldSurname(), 0.9);
            }
            case INVESTMENT_FUND -> {
                return new InvestmentFund(HolderFactory.fetchID(), 0);
            }
            case COMPANY -> {
                var rand = RandomDataGenerator.getInstance();
                var date = rand.yieldDate();
                var address = Address.getRandomAddress();
                var name = rand.useCompanyName();
                return new Company(HolderFactory.fetchID(), 0, name, date, address, 0, 0 ,0, 0,0);
            }
            default -> {
                //raise exception
            }
        }
        return null;
    }
}
