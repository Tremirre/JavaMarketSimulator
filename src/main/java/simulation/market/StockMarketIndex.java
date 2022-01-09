package main.java.simulation.market;

import main.java.simulation.holders.Company;

import java.util.ArrayList;
import java.util.HashSet;

public class StockMarketIndex {
    private HashSet<Company> companies;

    public StockMarketIndex() {
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

    public HashSet<Company> getCompanies() {
        return companies;
    }

    public static ArrayList<StockMarketIndex> splitCompaniesIntoIndexes(Company[] companies) {
        ArrayList<StockMarketIndex> indexes = new ArrayList<>();
        final int indexSize = 5;
        int current = 0;
        indexes.add(new StockMarketIndex());
        for (Company company : companies) {
            indexes.get(indexes.size() - 1).addCompany(company);
            if (current++ >= indexSize) {
                indexes.add(new StockMarketIndex());
                current = 0;
            }
        }
        return indexes;
    }
}
