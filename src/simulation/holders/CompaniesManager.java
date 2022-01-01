package simulation.holders;

import java.util.ArrayList;

public class CompaniesManager {
    private static CompaniesManager instance;
    ArrayList<Company> companies;

    private CompaniesManager() {
        this.companies = new ArrayList<>();
    }

    public Company getCompanyByID(int id) {
        for (var company : companies) {
            if (company.getID() == id) {
                return company;
            }
        }
        throw new RuntimeException("INVALID COMPANY ID PROVIDED!");
    }

    public static CompaniesManager getInstance() {
        if (instance == null) {
            instance = new CompaniesManager();
        }
        return instance;
    }

    public void processEndDay() {
        for (var company : companies) {
            company.saveAndResetStockTransactionsData();
        }
    }

    public Company createNewCompany() {
        var newCompany = new RandomHolderFactory().createCompany();
        this.companies.add(newCompany);
        return newCompany;
    }
}
