package stock;

import management.NumFormat;
import java.time.LocalDateTime;

public class Quote {
    private String symbol, name, exchange, currency, datetime;
    private LocalDateTime last_quote_at;
    private double open, high, low, close, volume, previous_close, change, percent_change;
    private FiftyTwoWeek fifty_two_week;

    public Quote(String symbol, String name, String exchange, String currency, String datetime, LocalDateTime last_quote_at,
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
                "\nOpen: " + NumFormat.formatCurrency(open) +
                "\nHigh: " + NumFormat.formatCurrency(high) +
                "\nLow: " + NumFormat.formatCurrency(low) +
                "\nClose: " + NumFormat.formatCurrency(close) +
                "\nVolume: " + NumFormat.formatNumber(volume) +
                "\nPrevious Close: " + NumFormat.formatCurrency(previous_close) +
                "\nChange: " + NumFormat.formatCurrency(change) +
                "\nPercent Change: " + NumFormat.formatPercent(percent_change) +
                "\n--- 52-Week Review ---" + fifty_two_week;
    }
}