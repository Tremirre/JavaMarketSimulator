package simulation.asset;

import simulation.holders.Company;

public class StockData extends AssetData{
    final private Company company;

    protected StockData(int id, String name, double openingPrice, Company company) {
        super(id, name, openingPrice);
        this.company = company;
        this.splittable = false;
    }

    public Company getCompany() {
        return this.company;
    }
}
