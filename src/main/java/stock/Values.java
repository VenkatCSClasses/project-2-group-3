package stock;

import misc.NumFormat;

public class Values {
    private String datetime;
    private double open, high, low, close, volume;

    public Values(String datetime, double open, double high, double low, double close, double volume) {
        this.datetime = datetime;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    public String getDatetime() { return datetime; }
    public double getOpen()     { return open; }
    public double getHigh()     { return high; }
    public double getLow()      { return low; }
    public double getClose()    { return close; }
    public double getVolume()   { return volume; }

    @Override
    public String toString() {
        return "Date and Time: " + datetime + 
                "\nOpen Price: " + NumFormat.formatCurrency(open) +
                "\nHigh: " + NumFormat.formatCurrency(high) +
                "\nLow: " + NumFormat.formatCurrency(low) +
                "\nClose: " + NumFormat.formatCurrency(close) +
                "\nVolume: " + NumFormat.formatNumber(volume);
    }
}