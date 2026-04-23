package stock;
import misc.NumFormat;

public class FiftyTwoWeek {
    private double low, high, low_change, high_change, low_change_percent, high_change_percent;
    private String range;

    public FiftyTwoWeek(double low, double high, double low_change, double high_change, double low_change_percent, 
        double high_change_percent, String range) {
        this.low = low;
        this.high = high;
        this.low_change = low_change;
        this.high_change = high_change;
        this.low_change_percent = low_change_percent;
        this.high_change_percent = high_change_percent;
        this.range = range;

    }

    @Override
    public String toString() {
        return "\nLow: " + NumFormat.formatCurrency(low) +
                "\nHigh: " + NumFormat.formatCurrency(high) +
                "\nLow Change: " + NumFormat.formatCurrency(low_change) +
                "\nHigh Change: " + NumFormat.formatCurrency(high_change) +
                "\nLow Percent Change:  " + NumFormat.formatPercent(low_change_percent) +
                "\nHigh Percent Change: " + NumFormat.formatPercent(high_change_percent) +
                "\nRange: " + range;
    }
}