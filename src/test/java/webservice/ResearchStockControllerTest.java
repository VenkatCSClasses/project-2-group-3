package webservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;

import trade.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ResearchStockControllerTest {

    private ResearchStockController controller;
    private ResearchStockService mockService;
    private MockHttpSession loggedInSession;
    private MockHttpSession emptySession;

    @BeforeEach
    void setUp() {
        mockService = mock(ResearchStockService.class);
        controller = new ResearchStockController(mockService);

        ResearchStock fakeStock = new ResearchStock();
        fakeStock.setTicker("AAPL");
        fakeStock.setCompanyName("Apple Inc.");
        fakeStock.setLastClosingPrice(210.15);
        fakeStock.setLastOpeningPrice(208.40);
        fakeStock.setVolume(58234120);
        fakeStock.setOneDayPriceChange(1.75);
        fakeStock.setOneDayPercentChange(0.84);

        when(mockService.getStockResearch(anyString())).thenReturn(fakeStock);

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
