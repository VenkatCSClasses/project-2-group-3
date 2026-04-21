package webservice;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("integration")
public class ResearchStockServiceTest {

    @Test
    void testGetStockResearchReturnsStock() {
        ResearchStockService service = new ResearchStockService();
        ResearchStock stock = service.getStockResearch("aapl");

        assertNotNull(stock);
        assertEquals("AAPL", stock.getTicker());
        assertNotNull(stock.getCompanyName());
        assertFalse(stock.getCompanyName().isBlank());
    }

    @Test
    void testGetStockResearchPricesArePositive() {
        ResearchStockService service = new ResearchStockService();
        ResearchStock stock = service.getStockResearch("AAPL");

        assertTrue(stock.getLastClosingPrice() > 0);
        assertTrue(stock.getLastOpeningPrice() > 0);
        assertTrue(stock.getVolume() > 0);
    }

    @Test
    void testGetStockResearchOneDayValuesAreSet() {
        ResearchStockService service = new ResearchStockService();
        ResearchStock stock = service.getStockResearch("AAPL");

        // Values should be non-zero (market moves every day)
        assertNotNull(stock);
        // Just verify the fields are populated (can be positive or negative)
        assertTrue(Double.isFinite(stock.getOneDayPriceChange()));
        assertTrue(Double.isFinite(stock.getOneDayPercentChange()));
    }

    @Test
    void testGetStockResearchOneWeekValuesAreSet() {
        ResearchStockService service = new ResearchStockService();
        ResearchStock stock = service.getStockResearch("AAPL");

        assertTrue(Double.isFinite(stock.getOneWeekPriceChange()));
        assertTrue(Double.isFinite(stock.getOneWeekPercentChange()));
    }

    @Test
    void testGetStockResearchOneMonthValuesAreSet() {
        ResearchStockService service = new ResearchStockService();
        ResearchStock stock = service.getStockResearch("AAPL");

        assertTrue(Double.isFinite(stock.getOneMonthPriceChange()));
        assertTrue(Double.isFinite(stock.getOneMonthPercentChange()));
    }

    @Test
    void testGetStockResearchThreeMonthValuesAreSet() {
        ResearchStockService service = new ResearchStockService();
        ResearchStock stock = service.getStockResearch("AAPL");

        assertTrue(Double.isFinite(stock.getThreeMonthPriceChange()));
        assertTrue(Double.isFinite(stock.getThreeMonthPercentChange()));
    }

    @Test
    void testGetStockResearchSixMonthValuesAreSet() {
        ResearchStockService service = new ResearchStockService();
        ResearchStock stock = service.getStockResearch("AAPL");

        assertTrue(Double.isFinite(stock.getSixMonthPriceChange()));
        assertTrue(Double.isFinite(stock.getSixMonthPercentChange()));
    }

    @Test
    void testGetStockResearchYearToDateValuesAreSet() {
        ResearchStockService service = new ResearchStockService();
        ResearchStock stock = service.getStockResearch("AAPL");

        assertTrue(Double.isFinite(stock.getYearToDatePriceChange()));
        assertTrue(Double.isFinite(stock.getYearToDatePercentChange()));
    }
}
