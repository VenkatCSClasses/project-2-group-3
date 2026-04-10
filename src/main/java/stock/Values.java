package stock;

public class Values {
    private String datetime, open, high, low, close, volume;

    public Values(String datetime, String open, String high, String low, String close, String volume) {
        this.datetime = datetime;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "Date and Time: " + datetime + 
                "\nOpen Price: " + open +
                "\nHigh: " + high +
                "\nLow: " + low +
                "\nClose: " + close +
                "\nVolume: " + volume;
    }
}