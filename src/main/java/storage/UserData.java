package storage;

import trade.Portfolio;

/**
 * Persisted per-user state: credentials, cash balance, and portfolio.
 * Stored at data/users/{username}.json via JsonFileManager.
 */
public class UserData {
    private String username;
    private String password;
    private double cashBalance;
    private Portfolio portfolio;

    public UserData() {}

    public UserData(String username, String password, double cashBalance, Portfolio portfolio) {
        this.username = username;
        this.password = password;
        this.cashBalance = cashBalance;
        this.portfolio = portfolio;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public double getCashBalance() { return cashBalance; }
    public Portfolio getPortfolio() { return portfolio; }

    public void setCashBalance(double cashBalance) { this.cashBalance = cashBalance; }
    public void setPortfolio(Portfolio portfolio) { this.portfolio = portfolio; }
}
