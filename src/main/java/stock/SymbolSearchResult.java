package stock;

public class SymbolSearchResult {
    private String symbol, instrument_name, exchange, exchange_timezone, instrument_type, country, currency;

    public SymbolSearchResult(String symbol, String instrument_name, String exchange, String exchange_timezone,
                              String instrument_type, String country, String currency) {
        this.symbol = symbol;
        this.instrument_name = instrument_name;
        this.exchange = exchange;
        this.exchange_timezone = exchange_timezone;
        this.instrument_type = instrument_type;
        this.country = country;
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Symbol: " + symbol +
               "\nCompany Name: " + instrument_name +
               "\nExchange: " + exchange +
               "\nExchange Timezone: " + exchange_timezone +
               "\nInstrument Type: " + instrument_type +
               "\nCountry: " + country +
               "\nCurrency: " + currency;
    }
}