package stock;

public class Meta {
    private String symbol, interval, currency, exchange_timezone, exchange, type;

    public Meta(String symbol, String interval, String currency, String exchange_timezone, String exchange, String type) {
        this.symbol = symbol;
        this.interval = interval;
        this.currency = currency;
        this.exchange_timezone = exchange_timezone;
        this.exchange = exchange;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Symbol: " + symbol + 
                "\nInterval: " + interval +
                "\nCurrency: " + currency +
                "\nExchange Timezone: " + exchange_timezone +
                "\nExchange: " + exchange +
                "\nType: " + type;
    }
}