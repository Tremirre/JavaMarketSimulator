package simulation.market;

import simulation.holders.Company;

import java.util.ArrayList;
import java.util.HashSet;

public class StockMarketIndex {
    private String name;
    private HashSet<Company> companies;

    public StockMarketIndex(String name) {
        this.name = name;
        this.companies = new HashSet<>();
    }

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
