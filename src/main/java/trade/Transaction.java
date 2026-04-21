package trade;

public class Transaction {
    private String transactionDate;
    private String transactionType;
    private String ticker;
    private double shares;
    private double transactionPrice;
    private double amountInvested;

    public Transaction(String transactionType, String ticker,
        double shares, double transactionPrice, double amountInvested) {
        this.transactionDate = java.time.LocalDate.now().toString();
        this.transactionType = transactionType;
        this.ticker = ticker;
        this.shares = shares;
        this.transactionPrice = transactionPrice;
        this.amountInvested = amountInvested;
       }
    
    public String getTicker() {
        return ticker;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public double getShares() {
        return shares;
    }

    public double getTransactionPrice() {
        return transactionPrice;
    }

    public double getAmountInvested() {
        return amountInvested;
    }
}