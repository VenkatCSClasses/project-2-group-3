package stock;

public class FiftyTwoWeek {
    private String low, high, low_change, high_change, low_change_percent, high_change_percent, range;

    public FiftyTwoWeek(String low, String high, String low_change, String high_change, String low_change_percent, 
        String high_change_percent, String range) {
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
        return "Low: " + low +
                "\nHigh: " + high +
                "\nLow Change: " + low_change +
                "\nHigh Change: " + high_change +
                "\nLow Change Percent:  " + low_change_percent +
                "\nHigh Change Percent: " + high_change_percent +
                "\nRange: " + range;
    }
}