package trade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

public class ResearchStockServiceTest {

    @Test
    void testGetStockResearchReturnsStock() {
        ResearchStockService service = new ResearchStockService();
        ResearchStock stock = service.getStockResearch("aapl");

        assertNotNull(stock);
        assertEquals("AAPL", stock.getTicker());
        assertEquals("Apple Inc.", stock.getCompanyName());
        assertEquals(210.15, stock.getLastClosingPrice());
        assertEquals(208.40, stock.getLastOpeningPrice());
        assertEquals(58234120, stock.getVolume());
    }

    @Test
    void testGetStockResearchOneDayValues() {
        ResearchStockService service = new ResearchStockService();
        ResearchStock stock = service.getStockResearch("AAPL");

        assertEquals(1.75, stock.getOneDayPriceChange());
        assertEquals(0.84, stock.getOneDayPercentChange());
    }

    @Test
    void testGetStockResearchOneWeekValues() {
        ResearchStockService service = new ResearchStockService();
        ResearchStock stock = service.getStockResearch("AAPL");

        assertEquals(4.20, stock.getOneWeekPriceChange());
        assertEquals(2.04, stock.getOneWeekPercentChange());
    }

    @Test
    void testGetStockResearchOneMonthValues() {
        ResearchStockService service = new ResearchStockService();
        ResearchStock stock = service.getStockResearch("AAPL");

        assertEquals(9.35, stock.getOneMonthPriceChange());
        assertEquals(4.66, stock.getOneMonthPercentChange());
    }

    @Test
    void testGetStockResearchThreeMonthValues() {
        ResearchStockService service = new ResearchStockService();
        ResearchStock stock = service.getStockResearch("AAPL");

        assertEquals(18.10, stock.getThreeMonthPriceChange());
        assertEquals(9.42, stock.getThreeMonthPercentChange());
    }

    @Test
    void testGetStockResearchSixMonthValues() {
        ResearchStockService service = new ResearchStockService();
        ResearchStock stock = service.getStockResearch("AAPL");

        assertEquals(26.55, stock.getSixMonthPriceChange());
        assertEquals(14.46, stock.getSixMonthPercentChange());
    }

    @Test
    void testGetStockResearchYearToDateValues() {
        ResearchStockService service = new ResearchStockService();
        ResearchStock stock = service.getStockResearch("AAPL");

        assertEquals(22.30, stock.getYearToDatePriceChange());
        assertEquals(11.87, stock.getYearToDatePercentChange());
    }
}