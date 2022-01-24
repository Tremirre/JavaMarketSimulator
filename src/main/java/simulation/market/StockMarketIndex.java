package simulation.market;

import simulation.holders.Company;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Class representing a stock market index.
 */
public class StockMarketIndex {
    /**
     * Name and identifier of the index.
     */
    private String name;
    /**
     * Set of companies that are a part of the index.
     */
    private HashSet<Company> companies;

    /**
     * Initializes companies container, sets index name.
     * @param name index name.
     */
    public StockMarketIndex(String name) {
        this.name = name;
        this.companies = new HashSet<>();
    }

    /**
     * Adds new company to the index.
     * @param company
     */
    public void addCompany(Company company) {
        companies.add(company);
    }

    /**
     * Removes the company from the index.
     * @param company
     */
    public void removeCompany(Company company) {
        companies.remove(company);
    }

    /**
     * Calculates the index value based on the capitals of its companies.
     * @return sum of capitals of the companies in the index.
     */
    public double calculateIndexValue() {
        double total = 0;
        for (var company : companies) {
            total += company.getCapital();
        }
        return total;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public HashSet<Company> getCompanies() {
        return companies;
    }
}
