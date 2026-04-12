package trade;
import java.util.ArrayList;

public class Portfolio {
    private int portfolioID;
    private ArrayList<Investment> investments;
    private int nextInvestmentID;

    public Portfolio() {
        this.investments = new ArrayList<>();
        this.nextInvestmentID = 1;
    }

    public ArrayList<Investment> getInvestments() {
        return investments;
    }

    public void addInvestment(Investment inv) {
        investments.add(inv);
    }

    public void removeInvestment(Investment inv) {
        investments.remove(inv);
    }

    public int generateInvestmentID() {
        return nextInvestmentID++;
    }

    public double getTotalValue() {
        double total = 0;
        for (Investment inv : investments) {
            total += inv.getValue();
        }
        return total;
    }

    public double getTotalChange() {
        double totalCost = 0;
        double totalValue = 0;

        for (Investment inv : investments) {
            totalCost += inv.getValue() / (1 + inv.getPercentChange() / 100);
            totalValue += inv.getValue();
        }

        if (totalCost == 0) return 0;

        return ((totalValue - totalCost) / totalCost) * 100;
    }
}