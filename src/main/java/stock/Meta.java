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

    public String getSymbol()            { return symbol; }
    public String getInterval()          { return interval; }
    public String getCurrency()          { return currency; }
    public String getExchange_timezone() { return exchange_timezone; }
    public String getExchange()          { return exchange; }
    public String getType()              { return type; }

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