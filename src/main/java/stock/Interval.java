package stock;

public enum Interval {
    ONE_MIN("1min"),
    FIVE_MIN("5min"),
    FIFTEEN_MIN("15min"),
    THIRTY_MIN("30min"),
    FORTY_FIVE_MIN("45min"),
    ONE_HOUR("1h"),
    TWO_HOUR("2h"),
    FOUR_HOUR("4h"),
    FIVE_HOUR("5h"),
    ONE_DAY("1day"),
    ONE_WEEK("1week"),
    ONE_MONTH("1month");

    private String value;

    Interval(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}