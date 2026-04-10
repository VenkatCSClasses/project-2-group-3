package trade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;

import static org.junit.jupiter.api.Assertions.*;

public class ResearchStockControllerTest {

    private ResearchStockController controller;
    private MockHttpSession loggedInSession;
    private MockHttpSession emptySession;

    @BeforeEach
    void setUp() {
        controller = new ResearchStockController(new ResearchStockService());

        loggedInSession = new MockHttpSession();
        loggedInSession.setAttribute("username", "alice");

        emptySession = new MockHttpSession();
    }

    @Test
    void getStockReturns401WhenNotLoggedIn() {
        ResponseEntity<?> response = controller.getStock("aapl", emptySession);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void getStockReturnsStockWhenLoggedIn() {
        ResponseEntity<?> response = controller.getStock("aapl", loggedInSession);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getStockReturnsCorrectTicker() {
        ResponseEntity<?> response = controller.getStock("aapl", loggedInSession);
        ResearchStock stock = (ResearchStock) response.getBody();
        assertEquals("AAPL", stock.getTicker());
    }

    @Test
    void getStockReturnsCorrectCompanyName() {
        ResponseEntity<?> response = controller.getStock("aapl", loggedInSession);
        ResearchStock stock = (ResearchStock) response.getBody();
        assertEquals("Apple Inc.", stock.getCompanyName());
    }

    @Test
    void getStockReturnsCorrectPrices() {
        ResponseEntity<?> response = controller.getStock("aapl", loggedInSession);
        ResearchStock stock = (ResearchStock) response.getBody();
        assertEquals(210.15, stock.getLastClosingPrice());
        assertEquals(208.40, stock.getLastOpeningPrice());
    }

    @Test
    void getStockReturnsCorrectVolume() {
        ResponseEntity<?> response = controller.getStock("aapl", loggedInSession);
        ResearchStock stock = (ResearchStock) response.getBody();
        assertEquals(58234120, stock.getVolume());
    }
}
