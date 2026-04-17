package trade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class ResearchStockTest {

    @Test
    void noArgConstructorCreatesInstance() {
        ResearchStock stock = new ResearchStock();
        assertNotNull(stock);
    }

    @Test
    void tickerGetterAndSetter() {
        ResearchStock stock = new ResearchStock();
        stock.setTicker("AAPL");
        assertEquals("AAPL", stock.getTicker());
    }

    @Test
    void companyNameGetterAndSetter() {
        ResearchStock stock = new ResearchStock();
        stock.setCompanyName("Apple Inc.");
        assertEquals("Apple Inc.", stock.getCompanyName());
    }

    @Test
    void lastClosingPriceGetterAndSetter() {
        ResearchStock stock = new ResearchStock();
        stock.setLastClosingPrice(210.15);
        assertEquals(210.15, stock.getLastClosingPrice());
    }

    @Test
    void lastOpeningPriceGetterAndSetter() {
        ResearchStock stock = new ResearchStock();
        stock.setLastOpeningPrice(208.40);
        assertEquals(208.40, stock.getLastOpeningPrice());
    }

    @Test
    void volumeGetterAndSetter() {
        ResearchStock stock = new ResearchStock();
        stock.setVolume(58234120L);
        assertEquals(58234120L, stock.getVolume());
    }

    @Test
    void oneDayPriceChangeGetterAndSetter() {
        ResearchStock stock = new ResearchStock();
        stock.setOneDayPriceChange(1.75);
        assertEquals(1.75, stock.getOneDayPriceChange());
    }

    @Test
    void oneDayPercentChangeGetterAndSetter() {
        ResearchStock stock = new ResearchStock();
        stock.setOneDayPercentChange(0.84);
        assertEquals(0.84, stock.getOneDayPercentChange());
    }

    @Test
    void oneWeekPriceChangeGetterAndSetter() {
        ResearchStock stock = new ResearchStock();
        stock.setOneWeekPriceChange(4.20);
        assertEquals(4.20, stock.getOneWeekPriceChange());
    }

    @Test
    void oneWeekPercentChangeGetterAndSetter() {
        ResearchStock stock = new ResearchStock();
        stock.setOneWeekPercentChange(2.04);
        assertEquals(2.04, stock.getOneWeekPercentChange());
    }

    @Test
    void oneMonthPriceChangeGetterAndSetter() {
        ResearchStock stock = new ResearchStock();
        stock.setOneMonthPriceChange(9.35);
        assertEquals(9.35, stock.getOneMonthPriceChange());
    }

    @Test
    void oneMonthPercentChangeGetterAndSetter() {
        ResearchStock stock = new ResearchStock();
        stock.setOneMonthPercentChange(4.66);
        assertEquals(4.66, stock.getOneMonthPercentChange());
    }

    @Test
    void threeMonthPriceChangeGetterAndSetter() {
        ResearchStock stock = new ResearchStock();
        stock.setThreeMonthPriceChange(18.10);
        assertEquals(18.10, stock.getThreeMonthPriceChange());
    }

    @Test
    void threeMonthPercentChangeGetterAndSetter() {
        ResearchStock stock = new ResearchStock();
        stock.setThreeMonthPercentChange(9.42);
        assertEquals(9.42, stock.getThreeMonthPercentChange());
    }

    @Test
    void sixMonthPriceChangeGetterAndSetter() {
        ResearchStock stock = new ResearchStock();
        stock.setSixMonthPriceChange(26.55);
        assertEquals(26.55, stock.getSixMonthPriceChange());
    }

    @Test
    void sixMonthPercentChangeGetterAndSetter() {
        ResearchStock stock = new ResearchStock();
        stock.setSixMonthPercentChange(14.46);
        assertEquals(14.46, stock.getSixMonthPercentChange());
    }

    @Test
    void yearToDatePriceChangeGetterAndSetter() {
        ResearchStock stock = new ResearchStock();
        stock.setYearToDatePriceChange(22.30);
        assertEquals(22.30, stock.getYearToDatePriceChange());
    }

    @Test
    void yearToDatePercentChangeGetterAndSetter() {
        ResearchStock stock = new ResearchStock();
        stock.setYearToDatePercentChange(11.87);
        assertEquals(11.87, stock.getYearToDatePercentChange());
    }
}
