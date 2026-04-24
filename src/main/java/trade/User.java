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
}
