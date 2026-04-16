package trade;

public class User {
    private String username;
    private double cashBalance;
    private Portfolio portfolio;

    public User(String username, double cashBalance) {
        this.username = username;
        this.cashBalance = cashBalance;
        this.portfolio = new Portfolio();
    }

    public String getUsername() {
        return username;
    }

    public double getCashBalance() {
        return cashBalance;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public void setCashBalance(double cashBalance) {
        this.cashBalance = cashBalance;
    }

    /**
     * Buy shares of a stock at the given live price.
     * @param method "shares" to specify a share count, "dollars" to specify a dollar amount
     * @return true if the purchase succeeded, false if insufficient funds or invalid amount
     */
    public boolean purchaseStock(String ticker, String companyName, double livePrice,
                                  String method, double amount) {
        double sharesToBuy;
        if (method.equalsIgnoreCase("shares")) {
            sharesToBuy = amount;
        } else {
            sharesToBuy = amount / livePrice;
        }

        if (sharesToBuy <= 0) return false;

        double totalCost = sharesToBuy * livePrice;
        if (totalCost > cashBalance) return false;

        cashBalance -= totalCost;

        Investment existing = findInvestment(ticker);
        if (existing != null) {
            existing.addShares(sharesToBuy, livePrice);
            existing.setCurrentPrice(livePrice);
        } else {
            String today = java.time.LocalDate.now().toString();
            Investment newInv = new Investment(ticker, companyName, today,
                    sharesToBuy, livePrice, totalCost);
            newInv.setCurrentPrice(livePrice);
            portfolio.addInvestment(newInv);
        }
        return true;
    }

    /**
     * Sell shares of a stock at the given live price.
     * @param method "shares" to specify a share count, "dollars" to specify a dollar amount
     * @return true if the sale succeeded, false if not enough shares or not found
     */
    public boolean sellStock(String ticker, double livePrice, String method, double amount) {
        Investment investment = findInvestment(ticker);
        if (investment == null) return false;

        double sharesToSell;
        if (method.equalsIgnoreCase("shares")) {
            sharesToSell = amount;
        } else {
            sharesToSell = amount / livePrice;
        }

        if (sharesToSell <= 0 || sharesToSell > investment.getShares()) return false;

        double proceeds = sharesToSell * livePrice;
        cashBalance += proceeds;

        investment.removeShares(sharesToSell);
        if (investment.getShares() <= 0) {
            portfolio.removeInvestment(investment);
        }
        return true;
    }

    public Investment findInvestment(String ticker) {
        for (Investment inv : portfolio.getInvestments()) {
            if (inv.getTicker().equalsIgnoreCase(ticker)) {
                return inv;
            }
        }
        return null;
    }
}
