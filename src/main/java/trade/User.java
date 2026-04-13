package trade;
import java.util.ArrayList;

public class User {
    private int userID;
    private String password;
    private double cashBalance;
    private Portfolio portfolio;

    public User(int userID, String password, double cashBalance) {
        this.userID = userID;
        this.password = password;
        this.cashBalance = cashBalance;
        this.portfolio = new Portfolio();
    }

    public double getBalance() {
        return cashBalance;
    }

    public void purchaseStock(String ticker, String method, double amount) {
        Stock stock = API.getData(ticker);
        double price = stock.getLastClosingPrice();

        double sharesToBuy;

        if (method.equalsIgnoreCase("shares")) {
            sharesToBuy = amount;
        } else {
            sharesToBuy = amount / price;
        }

        double totalCost = sharesToBuy * price;

        if (totalCost > cashBalance) {
            System.out.println("Not enough balance.");
            return;
        }

        cashBalance -= totalCost;

        Investment existing = findInvestment(ticker);

        if (existing != null) {
            existing.addShares(sharesToBuy, price);
        } else {
            Investment newInv = new Investment(
                portfolio.generateInvestmentID(),
                stock,
                sharesToBuy,
                totalCost,
                new java.util.Date()
            );
            portfolio.addInvestment(newInv);
        }
    }

    public void sellStock(String ticker, String method, double amount) {
        Investment investment = findInvestment(ticker);

        if (investment == null) {
            System.out.println("No such stock in portfolio.");
            return;
        }

        double price = investment.getStock().getLastClosingPrice();
        double sharesToSell;

        if (method.equalsIgnoreCase("shares")) {
            sharesToSell = amount;
        } else {
            sharesToSell = amount / price;
        }

        if (sharesToSell > investment.getShares()) {
            System.out.println("Not enough shares.");
            return;
        }

        double totalValue = sharesToSell * price;
        cashBalance += totalValue;

        investment.removeShares(sharesToSell, price);

        if (investment.getShares() == 0) {
            portfolio.removeInvestment(investment);
        }
    }

    private Investment findInvestment(String ticker) {
        for (Investment inv : portfolio.getInvestments()) {
            if (inv.getStock().getTicker().equalsIgnoreCase(ticker)) {
                return inv;
            }
        }
        return null;
    }
    public void viewPortfolio() {
    System.out.println("Portfolio:");

    for (Investment inv : portfolio.getInvestments()) {
        Stock stock = inv.getStock();

        System.out.println("Ticker: " + stock.getTicker());
        System.out.println("Shares: " + inv.getShares());
        System.out.println("Value: $" + inv.getValue());
        System.out.println("Change: " + inv.getPercentChange() + "%");
        System.out.println("------------------------");
    }

    System.out.println("Total Value: $" + portfolio.getTotalValue());
    System.out.println("Total Change: " + portfolio.getTotalChange() + "%");
    }

    public void researchStock(String ticker) {
        Stock stock = API.getData(ticker);

    if (stock == null) {
        System.out.println("Stock not found.");
        return;
    }

    System.out.println("Stock Info:");
    System.out.println("Ticker: " + stock.getTicker());
    System.out.println("Company: " + stock.getCompanyName());
    System.out.println("Last Close: $" + stock.getLastClosingPrice());
    System.out.println("Open: $" + stock.getLastOpeningPrice());
    System.out.println("Volume: " + stock.getVolume());
    }
}