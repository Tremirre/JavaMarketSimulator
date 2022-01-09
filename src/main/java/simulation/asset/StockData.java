package main.java.simulation.asset;

import main.java.simulation.holders.Company;

public class StockData extends AssetData {
    final private Company company;

    protected StockData(int id, String name, double openingPrice, Company company) {
        super(id, name, openingPrice);
        this.company = company;
        this.splittable = false;
    }

    public Company getCompany() {
        return this.company;
    }

    @Override
    public double getQualityMeasure() {
        return Math.max(Math.min(2 * this.company.getProfit() / this.company.getRevenue(), 1), 0);
    }
}
