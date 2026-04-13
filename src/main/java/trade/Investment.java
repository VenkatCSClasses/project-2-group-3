package trade;

public class Investment {
    private String ticker;
    private String companyName;
    private String purchaseDate;
    private double shares;
    private double purchasePrice;
    private double amountInvested;

    public Investment(String ticker, String companyName, String purchaseDate,
                      double shares, double purchasePrice, double amountInvested) {
        this.ticker = ticker;
        this.companyName = companyName;
        this.purchaseDate = purchaseDate;
        this.shares = shares;
        this.purchasePrice = purchasePrice;
        this.amountInvested = amountInvested;
    }

    public String getTicker() {
        return ticker;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public double getShares() {
        return shares;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public double getAmountInvested() {
        return amountInvested;
    }
}