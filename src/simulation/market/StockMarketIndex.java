package simulation.market;

import simulation.holders.Company;

import java.util.HashSet;

public class StockMarketIndex {
    HashSet<Company> companies;

    public void addCompany(Company company) {
        companies.add(company);
    }

    public void removeCompany(Company company) {
        companies.remove(company);
    }

    public double calculateMarketValue() {
        return 0.0;
    }

    public HashSet<Company> getCompanies() {
        return companies;
    }

}
