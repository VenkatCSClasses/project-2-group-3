package trade;

public class UserTrading {
    /**
     * Buy shares of a stock at the given live price.
     * @param method "shares" to specify a share count, "dollars" to specify a dollar amount
     * @return true if the purchase succeeded, false if insufficient funds or invalid amount
     */
    public static Investment purchaseStock(User user, String ticker, String companyName, double livePrice, String method, double amount) {
        if (livePrice <= 0 || amount <= 0) {
            return null;
        }

        double sharesToBuy;
        if (method.equalsIgnoreCase("shares")) {
            sharesToBuy = amount;
        } else if (method.equalsIgnoreCase("dollars")) {
            sharesToBuy = amount / livePrice;
        } else {
            return null;
        }

        if (sharesToBuy <= 0) {
            return null;
        }

        double totalCost = sharesToBuy * livePrice;
        if (totalCost > user.getCashBalance()) {
            return null;
        }

        user.setCashBalance(user.getCashBalance() - totalCost);

        String today = java.time.LocalDate.now().toString();
        int investmentId = user.getPortfolio().generateInvestmentID();

        Investment newInv = new Investment(investmentId, ticker, companyName, today, sharesToBuy, livePrice, totalCost, 0);
        newInv.setCurrentPrice(livePrice);
        user.getPortfolio().addInvestment(newInv);

        Transaction newTransaction = new Transaction("Buy", ticker, sharesToBuy, livePrice, totalCost);
        user.getTransactionLog().addTransaction(newTransaction);

        return newInv;
    }

    /**
     * Sell shares of a stock at the given live price.
     * @param method "shares" to specify a share count, "dollars" to specify a dollar amount
     * @return true if the sale succeeded, false if not enough shares or not found
     */
    public static boolean sellStock(User user, int investmentId, String ticker, double livePrice, String method, double amount) {
        Investment investment = findInvestmentById(user, investmentId);
        if (investment == null) {
            return false;
        }

        double sharesToSell;
        if (method.equalsIgnoreCase("shares")) {
            sharesToSell = amount;
        } else if (method.equalsIgnoreCase("dollars")) {
            sharesToSell = amount / livePrice;
        } else {
            return false;
        }

        final double EPSILON = 0.000001;

        double ownedShares = investment.getShares();

        if (sharesToSell <= 0 || sharesToSell > ownedShares + EPSILON) {
            return false;
        }

        // If the requested sale is basically the whole position, force it to exact full shares.
        // This prevents tiny leftovers like 0.00000000003 from staying in the portfolio.
        if (Math.abs(sharesToSell - ownedShares) <= EPSILON) {
            sharesToSell = ownedShares;
        }

        double proceeds = sharesToSell * livePrice;
        user.setCashBalance(user.getCashBalance() + proceeds);

        investment.removeShares(sharesToSell);

        Transaction newTransaction = new Transaction("Sell", ticker, sharesToSell, livePrice, proceeds);
        user.getTransactionLog().addTransaction(newTransaction);

        if (investment.getShares() <= EPSILON) {
            user.getPortfolio().removeInvestment(investment);
        }

        return true;
    }

    public static Investment findInvestment(User user, String ticker) {
        Portfolio portfolio = user.getPortfolio();
        for (Investment inv : portfolio.getInvestments()) {
            if (inv.getTicker().equalsIgnoreCase(ticker)) {
                return inv;
            }
        }
        return null;
    }

    public static Investment findInvestmentById(User user, int investmentId) {
        Portfolio portfolio = user.getPortfolio();
        for (Investment inv : portfolio.getInvestments()) { 
            if (inv.getInvestmentId() == investmentId) { 
                return inv; 
            }
        }
        return null;
    }
    
}