package trade;
import java.util.ArrayList;

public class Portfolio {
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

    public double getRealTotalValue() {
        double total = 0;

        for (Investment inv : investments) {
            if (inv.getInvestmentType() == 0) {
                total += inv.getValue();
            }
        }

        return total;
    }

    public double getRealTotalChange() {
        double totalValue = 0;
        double totalCostBasis = 0;

        for (Investment inv : investments) {
            if (inv.getInvestmentType() == 0) {
                totalValue += inv.getValue();
                totalCostBasis += inv.getShares() * inv.getPurchasePrice();
            }
        }

        if (totalCostBasis == 0) {
            return 0;
        }

        return ((totalValue - totalCostBasis) / totalCostBasis) * 100;
    }

    public double getWhatIfTotalValue() {
        double total = 0;

        for (Investment inv : investments) {
            if (inv.getInvestmentType() == 1) {
                total += inv.getValue();
            }
        }

        return total;
    }

    public double getWhatIfTotalChange() {
        double totalValue = 0;
        double totalCostBasis = 0;

        for (Investment inv : investments) {
            if (inv.getInvestmentType() == 1) {
                totalValue += inv.getValue();
                totalCostBasis += inv.getShares() * inv.getPurchasePrice();
            }
        }

        if (totalCostBasis == 0) {
            return 0;
        }

        return ((totalValue - totalCostBasis) / totalCostBasis) * 100;
    }
}