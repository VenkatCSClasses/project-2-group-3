package storage;

import trade.*;

/**
 * Persisted per-user state: credentials, cash balance, and portfolio.
 * Stored at data/users/{username}.json via JsonFileManager.
 */
public class UserData {
    private String username;
    private String password;
    private double cashBalance;
    private Portfolio portfolio;
    private TransactionLog transactionLog;

    public UserData() {}

    public UserData(String username, String password, double cashBalance, Portfolio portfolio, TransactionLog transactionLog) {
        this.username = username;
        this.password = password;
        this.cashBalance = cashBalance;
        this.portfolio = portfolio;
        this.transactionLog = transactionLog;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public double getCashBalance() { return cashBalance; }
    public Portfolio getPortfolio() { return portfolio; }
    public TransactionLog getTransactionLog() { return transactionLog; }

    public void setCashBalance(double cashBalance) { this.cashBalance = cashBalance; }
    public void setPortfolio(Portfolio portfolio) { this.portfolio = portfolio; }
    public void setTransactionLog(TransactionLog transactionLog) { this.transactionLog = transactionLog; }
}
