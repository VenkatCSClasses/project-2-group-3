package stock;

public class EODPrice {
    private String symbol;
    private String exchange;
    private String currency;
    private String datetime;
    private double close;

    public EODPrice(String symbol, String exchange, String currency, String datetime, double close) {
        this.symbol = symbol;
        this.exchange = exchange;
        this.currency = currency;
        this.datetime = datetime;
        this.close = close;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getExchange() {
        return exchange;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDatetime() {
        return datetime;
    }

    public double getClose() {
        return close;
    }

    @Override
    public String toString() {
        return "Symbol: " + symbol + 
                "\nExchange: " + exchange +
                "\nCurrency: " + currency +
                "\nDate: " + datetime +
                "\nClosing Price: " + close;
    }
}