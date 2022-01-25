package simulation.asset;

import simulation.holders.Company;

/**
 * Subclass of AssetData representing data of an asset associated with a company (a share of that company).
 */
public class StockData extends AssetData {
    /**
     * Reference to the company with which the asset is associated.
     */
    final private Company company;

    /**
     * Fills the asset with data.
     * @param id numerical id of the stock.
     * @param name name of the stock.
     * @param openingPrice initial price of the stock per 1 unit of it in DEFAULT STANDARD CURRENCY.
     * @param company reference to the company with which the asset is associated.
     */
    protected StockData(int id, String name, double openingPrice, Company company) {
        super(id, name, openingPrice);
        this.company = company;
        this.splittable = false;
    }

    public Company getCompany() {
        return this.company;
    }

    /**
     * {@inheritDoc}
     * For stock data it obtains the quality measure based on the profit and revenue of the company with which the
     * stock asset is associated.
     * @return
     */
    @Override
    public double getQualityMeasure() {
        return Math.max(Math.min(2 * this.company.getProfit() / this.company.getRevenue(), 1), 0);
    }
}
