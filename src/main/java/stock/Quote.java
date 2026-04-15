package stock;

public class Quote {
    private String symbol, name, exchange, currency, datetime, last_quote_at;
    private double open, high, low, close, volume, previous_close, change, percent_change;
    private FiftyTwoWeek fifty_two_week;

    public Quote(String symbol, String name, String exchange, String currency, String datetime, String last_quote_at,
        double open, double high, double low, double close, double volume, double previous_close, double change,
        double percent_change, FiftyTwoWeek fifty_two_week) {
        this.symbol = symbol;
        this.name = name;
        this.exchange = exchange;
        this.currency = currency;
        this.datetime = datetime;
        this.last_quote_at = last_quote_at;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.previous_close = previous_close;
        this.change = change;
        this.percent_change = percent_change;
        this.fifty_two_week = fifty_two_week;

    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Symbol: " + symbol + 
                "\nName: " + name +
                "\nExchange: " + exchange +
                "\nCurrency: " + currency +
                "\nDatetime: " + datetime +
                "\nLast Quote: " + last_quote_at +
                "\nOpen: " + open +
                "\nHigh: " + high +
                "\nLow: " + low +
                "\nClose: " + close +
                "\nVolume: " + volume +
                "\nPrevious Close: " + previous_close +
                "\nChange: " + change +
                "\nPercent Change: " + percent_change +
                "\n: " + fifty_two_week;
    }
}