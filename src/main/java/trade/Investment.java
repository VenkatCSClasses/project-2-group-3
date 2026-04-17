package trade;
import java.util.Date;

public class Investment {
    private int investmentID;
    private Stock stock;
    private double shares;
    private double cost;
    private Date date;

    public Investment(int investmentID, Stock stock, double shares, double cost, Date date) {
        this.investmentID = investmentID;
        this.stock = stock;
        this.shares = shares;
        this.cost = cost;
        this.date = date;
    }

    public double getValue() {
        return shares * stock.getLastClosingPrice();
    }

    public double getPercentChange() {
        if (cost == 0) return 0;
        return ((getValue() - cost) / cost) * 100;
    }

    public void addShares(double additionalShares, double pricePerShare) {
        double additionalCost = additionalShares * pricePerShare;
        this.cost += additionalCost;
        this.shares += additionalShares;
    }

    public void removeShares(double sharesToSell, double pricePerShare) {
        double proportion = sharesToSell / this.shares;
        this.cost -= this.cost * proportion;
        this.shares -= sharesToSell;
    }

    public Stock getStock() {
        return stock;
    }

    public double getShares() {
        return shares;
    }
}