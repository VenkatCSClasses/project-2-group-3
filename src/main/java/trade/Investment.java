package trade;

public class Investment {
    private int investmentId;
    private String ticker;
    private String companyName;
    private String purchaseDate;
    private double shares;
    private double purchasePrice;
    private double amountInvested;
    private double currentPrice;
    private int investmentType;

    public Investment(int investmentId, String ticker, String companyName, String purchaseDate,
                      double shares, double purchasePrice, double amountInvested, int investmentType) {
        this.investmentId = investmentId;
        this.ticker = ticker;
        this.companyName = companyName;
        this.purchaseDate = purchaseDate;
        this.shares = shares;
        this.purchasePrice = purchasePrice;
        this.amountInvested = amountInvested;
        this.currentPrice = purchasePrice;
        this.investmentType = investmentType; // 0 if it is a regular investment, 1 if it is a historical/what-if investment
    }

    public int getInvestmentId() {
        return investmentId;
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

    public double getCurrentPrice() {
        return currentPrice;
    }

    public int getInvestmentType() {
        return investmentType;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    // Weighted-average cost basis update when adding shares
    public void addShares(double newShares, double price) {
        double totalCost = (this.shares * this.purchasePrice) + (newShares * price);
        this.shares += newShares;
        this.purchasePrice = totalCost / this.shares;
        this.amountInvested = this.shares * this.purchasePrice;
    }

    // Reduce share count on sell
    public void removeShares(double sharesToSell) {
        this.shares -= sharesToSell;
        this.amountInvested = this.shares * this.purchasePrice;
    }

    public double getValue() {
        return shares * currentPrice;
    }

    public double getPercentChange() {
        if (purchasePrice == 0) return 0;
        return ((currentPrice - purchasePrice) / purchasePrice) * 100;
    }
}