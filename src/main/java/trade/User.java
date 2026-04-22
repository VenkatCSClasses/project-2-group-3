package trade;

public class User {
    private String username;
    private double cashBalance;
    private Portfolio portfolio;
    private TransactionLog transactionLog;

    public User(String username, double cashBalance) {
        this.username = username;
        this.cashBalance = cashBalance;
        this.portfolio = new Portfolio();
        this.transactionLog = new TransactionLog();
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

    public TransactionLog getTransactionLog() {
        return transactionLog;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public void setTransactionLog(TransactionLog transactionLog) {
        this.transactionLog = transactionLog;
    }

    public void setCashBalance(double cashBalance) {
        this.cashBalance = cashBalance;
    }

    /**
     * Buy shares of a stock at the given live price.
     * @param method "shares" to specify a share count, "dollars" to specify a dollar amount
     * @return true if the purchase succeeded, false if insufficient funds or invalid amount
     */
    public boolean purchaseStock(String ticker, String companyName, double livePrice, String method, double amount) {
        if (livePrice <= 0 || amount <= 0) {
            return false;
        }

        double sharesToBuy;
        if (method.equalsIgnoreCase("shares")) {
            sharesToBuy = amount;
        } else if (method.equalsIgnoreCase("dollars")) {
            sharesToBuy = amount / livePrice;
        } else {
            return false;
        }

        if (sharesToBuy <= 0) {
            return false;
        }

        double totalCost = sharesToBuy * livePrice;
        if (totalCost > cashBalance) {
            return false;
        }

        cashBalance -= totalCost;

        String today = java.time.LocalDate.now().toString();
        Investment newInv = new Investment(ticker, companyName, today, sharesToBuy, livePrice, totalCost);
        newInv.setCurrentPrice(livePrice);
        portfolio.addInvestment(newInv);

        Transaction newTransaction = new Transaction("Buy", ticker, sharesToBuy, livePrice, totalCost);
        transactionLog.addTransaction(newTransaction);

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
        } else if (method.equalsIgnoreCase("dollars")) {
            sharesToSell = amount / livePrice;
        } else {
            return false;
        }

        if (sharesToSell <= 0 || sharesToSell > investment.getShares()) return false;

        double proceeds = sharesToSell * livePrice;
        cashBalance += proceeds;

        investment.removeShares(sharesToSell);
        Transaction newTransaction = new Transaction("Sell", ticker, sharesToSell, livePrice, proceeds);
        transactionLog.addTransaction(newTransaction);

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
