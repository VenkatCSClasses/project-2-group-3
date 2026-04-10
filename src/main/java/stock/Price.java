package stock;

public class Price {
    private String symbol;
    private String price;

    public Price(String symbol, String price) {
        this.symbol = symbol;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Symbol: " + symbol + "\nPrice: " + price;
    }
}