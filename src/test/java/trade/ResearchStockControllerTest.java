package trade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

public class ResearchStockControllerTest {

    @Test
    void testGetStockReturnsResearchStock() {
        ResearchStockService service = new ResearchStockService();
        ResearchStockController controller = new ResearchStockController(service);

        ResearchStock stock = controller.getStock("aapl");

        assertNotNull(stock);
        assertEquals("AAPL", stock.getTicker());
        assertEquals("Apple Inc.", stock.getCompanyName());
    }

    @Test
    void testGetStockReturnsCorrectOpeningAndClosingPrice() {
        ResearchStockService service = new ResearchStockService();
        ResearchStockController controller = new ResearchStockController(service);

        ResearchStock stock = controller.getStock("aapl");

        assertEquals(210.15, stock.getLastClosingPrice());
        assertEquals(208.40, stock.getLastOpeningPrice());
    }

    @Test
    void testGetStockReturnsVolume() {
        ResearchStockService service = new ResearchStockService();
        ResearchStockController controller = new ResearchStockController(service);

        ResearchStock stock = controller.getStock("aapl");

        assertEquals(58234120, stock.getVolume());
    }
}
