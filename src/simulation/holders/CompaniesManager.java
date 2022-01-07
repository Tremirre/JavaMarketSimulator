package simulation.holders;

import simulation.market.Market;

import java.util.ArrayList;
import java.util.HashSet;

public final class CompaniesManager {
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

    public synchronized static CompaniesManager getInstance() {
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
        newCompany.start();
        return newCompany;
    }

    public InvestmentFund createNewFund() {
        var newFund = new RandomHolderFactory().createInvestmentFund();
        this.companies.add(newFund);
        newFund.start();
        return newFund;
    }

    public void giveAccessToMarkets(HashSet<Market> markets) {
        for (var company : companies) {
            company.giveAccessToMarkets(markets);
        }
    }

    public void stopCompanies() {
        for (var company : companies) {
            company.stopRunning();
        }
    }
}
