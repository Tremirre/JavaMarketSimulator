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
        double total = 0;
        for (var company : companies) {
            total += company.getCapital();
        }
        return total;
    }

    public HashSet<Company> getCompanies() {
        return companies;
    }

}
