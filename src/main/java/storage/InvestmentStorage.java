package storage;

import java.util.ArrayList;
import java.util.List;

import trade.Investment;

public class InvestmentStorage {
    private List<Investment> investments;

    public InvestmentStorage() {
        investments = new ArrayList<>();
    }

    public void addInvestment(Investment investment) {
        investments.add(investment);
    }

    public List<Investment> getInvestments() {
        return investments;
    }
}