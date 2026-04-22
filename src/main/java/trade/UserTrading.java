package trade;

public class UserTrading {
    /**
     * Buy shares of a stock at the given live price.
     * @param method "shares" to specify a share count, "dollars" to specify a dollar amount
     * @return true if the purchase succeeded, false if insufficient funds or invalid amount
     */
    public static boolean purchaseStock(User user, String ticker, String companyName, double livePrice, String method, double amount) {
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
        if (totalCost > user.getCashBalance()) {
            return false;
        }

        user.setCashBalance(user.getCashBalance() - totalCost);

        String today = java.time.LocalDate.now().toString();
        Investment newInv = new Investment(ticker, companyName, today, sharesToBuy, livePrice, totalCost, 0);
        newInv.setCurrentPrice(livePrice);
        user.getPortfolio().addInvestment(newInv);

        Transaction newTransaction = new Transaction("Buy", ticker, sharesToBuy, livePrice, totalCost);
        user.getTransactionLog().addTransaction(newTransaction);

        return true;
    }

    /**
     * Sell shares of a stock at the given live price.
     * @param method "shares" to specify a share count, "dollars" to specify a dollar amount
     * @return true if the sale succeeded, false if not enough shares or not found
     */
    public static boolean sellStock(User user, String ticker, double livePrice, String method, double amount) {
        Investment investment = findInvestment(user, ticker);
        if (investment == null) return false;

        double sharesToSell;
        if (method.equalsIgnoreCase("shares")) {
            sharesToSell = amount;
        } else if (method.equalsIgnoreCase("dollars")) {
            sharesToSell = amount / livePrice;
        } else {
            return false;
        }

        if (sharesToSell <= 0 || sharesToSell > investment.getShares()) {
            return false;
        }

        double proceeds = sharesToSell * livePrice;
        user.setCashBalance(user.getCashBalance() + proceeds);

        investment.removeShares(sharesToSell);
        Transaction newTransaction = new Transaction("Sell", ticker, sharesToSell, livePrice, proceeds);
        user.getTransactionLog().addTransaction(newTransaction);

        if (investment.getShares() <= 0) {
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
}