package webservice;

public class ResearchStock {
    private String ticker;
    private String companyName;
    private double lastClosingPrice;
    private double lastOpeningPrice;
    private long volume;

    private double oneDayPriceChange;
    private double oneDayPercentChange;

    private double oneWeekPriceChange;
    private double oneWeekPercentChange;

    private double oneMonthPriceChange;
    private double oneMonthPercentChange;

    private double threeMonthPriceChange;
    private double threeMonthPercentChange;

    private double sixMonthPriceChange;
    private double sixMonthPercentChange;

    private double yearToDatePriceChange;
    private double yearToDatePercentChange;

    public ResearchStock() {
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public double getLastClosingPrice() {
        return lastClosingPrice;
    }

    public void setLastClosingPrice(double lastClosingPrice) {
        this.lastClosingPrice = lastClosingPrice;
    }

    public double getLastOpeningPrice() {
        return lastOpeningPrice;
    }

    public void setLastOpeningPrice(double lastOpeningPrice) {
        this.lastOpeningPrice = lastOpeningPrice;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public double getOneDayPriceChange() {
        return oneDayPriceChange;
    }

    public void setOneDayPriceChange(double oneDayPriceChange) {
        this.oneDayPriceChange = oneDayPriceChange;
    }

    public double getOneDayPercentChange() {
        return oneDayPercentChange;
    }

    public void setOneDayPercentChange(double oneDayPercentChange) {
        this.oneDayPercentChange = oneDayPercentChange;
    }

    public double getOneWeekPriceChange() {
        return oneWeekPriceChange;
    }

    public void setOneWeekPriceChange(double oneWeekPriceChange) {
        this.oneWeekPriceChange = oneWeekPriceChange;
    }

    public double getOneWeekPercentChange() {
        return oneWeekPercentChange;
    }

    public void setOneWeekPercentChange(double oneWeekPercentChange) {
        this.oneWeekPercentChange = oneWeekPercentChange;
    }

    public double getOneMonthPriceChange() {
        return oneMonthPriceChange;
    }

    public void setOneMonthPriceChange(double oneMonthPriceChange) {
        this.oneMonthPriceChange = oneMonthPriceChange;
    }

    public double getOneMonthPercentChange() {
        return oneMonthPercentChange;
    }

    public void setOneMonthPercentChange(double oneMonthPercentChange) {
        this.oneMonthPercentChange = oneMonthPercentChange;
    }

    public double getThreeMonthPriceChange() {
        return threeMonthPriceChange;
    }

    public void setThreeMonthPriceChange(double threeMonthPriceChange) {
        this.threeMonthPriceChange = threeMonthPriceChange;
    }

    public double getThreeMonthPercentChange() {
        return threeMonthPercentChange;
    }

    public void setThreeMonthPercentChange(double threeMonthPercentChange) {
        this.threeMonthPercentChange = threeMonthPercentChange;
    }

    public double getSixMonthPriceChange() {
        return sixMonthPriceChange;
    }

    public void setSixMonthPriceChange(double sixMonthPriceChange) {
        this.sixMonthPriceChange = sixMonthPriceChange;
    }

    public double getSixMonthPercentChange() {
        return sixMonthPercentChange;
    }

    public void setSixMonthPercentChange(double sixMonthPercentChange) {
        this.sixMonthPercentChange = sixMonthPercentChange;
    }

    public double getYearToDatePriceChange() {
        return yearToDatePriceChange;
    }

    public void setYearToDatePriceChange(double yearToDatePriceChange) {
        this.yearToDatePriceChange = yearToDatePriceChange;
    }

    public double getYearToDatePercentChange() {
        return yearToDatePercentChange;
    }

    public void setYearToDatePercentChange(double yearToDatePercentChange) {
        this.yearToDatePercentChange = yearToDatePercentChange;
    }
}