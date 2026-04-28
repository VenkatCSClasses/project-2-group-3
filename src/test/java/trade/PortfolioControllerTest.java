package trade;

import api.GetYahooPrice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;
import webservice.UserService;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PortfolioControllerTest {

    private static final double DELTA = 0.0001;

    private PortfolioController controller;
    private UserService userService;
    private User user;
    private MockHttpSession loggedInSession;
    private MockHttpSession emptySession;

    @BeforeEach
    void setUp() {
        controller = new PortfolioController();
        userService = mock(UserService.class);

        user = new User("alice", 10000.0);

        loggedInSession = new MockHttpSession();
        loggedInSession.setAttribute("username", "alice");

        emptySession = new MockHttpSession();

        when(userService.getUser("alice")).thenReturn(user);

        ReflectionTestUtils.setField(controller, "userService", userService);
    }

    @Test
    void getPortfolioRequiresLogin() {
        ResponseEntity<?> response = controller.getPortfolio(emptySession);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Please log in first.", response.getBody());
    }

    @Test
    void getPortfolioReturnsCashInvestmentsAndTotalsWhenLoggedIn() {
        Investment investment = new Investment(
                1,
                "AAPL",
                "Apple Inc.",
                "2026-04-13",
                5.0,
                100.0,
                500.0,
                0
        );
        investment.setCurrentPrice(120.0);
        user.getPortfolio().addInvestment(investment);

        try (MockedStatic<GetYahooPrice> mockedPrice = mockStatic(GetYahooPrice.class)) {
            mockedPrice.when(() -> GetYahooPrice.run("AAPL")).thenReturn(120.0);

            ResponseEntity<?> response = controller.getPortfolio(loggedInSession);

            assertEquals(HttpStatus.OK, response.getStatusCode());

            Map<String, Object> body = asMap(response.getBody());

            assertEquals("alice", body.get("username"));
            assertEquals(10000.0, (double) body.get("cashBalance"), DELTA);
            assertEquals(600.0, (double) body.get("totalValue"), DELTA);
            assertEquals(20.0, (double) body.get("totalChange"), DELTA);

            List<?> investments = (List<?>) body.get("investments");
            assertEquals(1, investments.size());
        }
    }

    @Test
    void buyRequiresLogin() {
        PortfolioController.BuyRequest request =
                new PortfolioController.BuyRequest("AAPL", "Apple Inc.", "shares", 2.0);

        ResponseEntity<?> response = controller.buy(request, emptySession);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Please log in first.", response.getBody());
        verify(userService, never()).saveUser(any());
    }

    @Test
    void buyBySharesCreatesInvestmentAndSavesUser() {
        PortfolioController.BuyRequest request =
                new PortfolioController.BuyRequest("AAPL", "Apple Inc.", "shares", 3.0);

        try (MockedStatic<GetYahooPrice> mockedPrice = mockStatic(GetYahooPrice.class)) {
            mockedPrice.when(() -> GetYahooPrice.run("AAPL")).thenReturn(100.0);

            ResponseEntity<?> response = controller.buy(request, loggedInSession);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(1, user.getPortfolio().getInvestments().size());

            Investment investment = user.getPortfolio().getInvestments().get(0);
            assertEquals("AAPL", investment.getTicker());
            assertEquals("Apple Inc.", investment.getCompanyName());
            assertEquals(3.0, investment.getShares(), DELTA);
            assertEquals(100.0, investment.getPurchasePrice(), DELTA);
            assertEquals(9700.0, user.getCashBalance(), DELTA);
            assertEquals(1, user.getTransactionLog().size());

            verify(userService).saveUser(user);
        }
    }

    @Test
    void buyByDollarsCreatesInvestmentAndSavesUser() {
        PortfolioController.BuyRequest request =
                new PortfolioController.BuyRequest("MSFT", "Microsoft", "dollars", 1000.0);

        try (MockedStatic<GetYahooPrice> mockedPrice = mockStatic(GetYahooPrice.class)) {
            mockedPrice.when(() -> GetYahooPrice.run("MSFT")).thenReturn(200.0);

            ResponseEntity<?> response = controller.buy(request, loggedInSession);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(1, user.getPortfolio().getInvestments().size());

            Investment investment = user.getPortfolio().getInvestments().get(0);
            assertEquals("MSFT", investment.getTicker());
            assertEquals(5.0, investment.getShares(), DELTA);
            assertEquals(200.0, investment.getPurchasePrice(), DELTA);
            assertEquals(9000.0, user.getCashBalance(), DELTA);
            assertEquals(1, user.getTransactionLog().size());

            verify(userService).saveUser(user);
        }
    }

    @Test
    void buySameTickerCreatesSeparateInvestments() {
        PortfolioController.BuyRequest firstRequest =
                new PortfolioController.BuyRequest("NVDA", "NVIDIA Corp.", "dollars", 1000.0);

        PortfolioController.BuyRequest secondRequest =
                new PortfolioController.BuyRequest("NVDA", "NVIDIA Corp.", "dollars", 500.0);

        try (MockedStatic<GetYahooPrice> mockedPrice = mockStatic(GetYahooPrice.class)) {
            mockedPrice.when(() -> GetYahooPrice.run("NVDA")).thenReturn(100.0);

            ResponseEntity<?> firstResponse = controller.buy(firstRequest, loggedInSession);
            ResponseEntity<?> secondResponse = controller.buy(secondRequest, loggedInSession);

            assertEquals(HttpStatus.OK, firstResponse.getStatusCode());
            assertEquals(HttpStatus.OK, secondResponse.getStatusCode());

            assertEquals(2, user.getPortfolio().getInvestments().size());

            Investment first = user.getPortfolio().getInvestments().get(0);
            Investment second = user.getPortfolio().getInvestments().get(1);

            assertNotEquals(first.getInvestmentId(), second.getInvestmentId());
            assertEquals(10.0, first.getShares(), DELTA);
            assertEquals(5.0, second.getShares(), DELTA);
            assertEquals(8500.0, user.getCashBalance(), DELTA);

            verify(userService, times(2)).saveUser(user);
        }
    }

    @Test
    void buyFailsWithInvalidAmount() {
        PortfolioController.BuyRequest request =
                new PortfolioController.BuyRequest("AAPL", "Apple Inc.", "shares", 0.0);

        try (MockedStatic<GetYahooPrice> mockedPrice = mockStatic(GetYahooPrice.class)) {
            mockedPrice.when(() -> GetYahooPrice.run("AAPL")).thenReturn(100.0);

            ResponseEntity<?> response = controller.buy(request, loggedInSession);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("Purchase failed — insufficient funds or invalid amount.", response.getBody());
            assertEquals(0, user.getPortfolio().getInvestments().size());
            assertEquals(10000.0, user.getCashBalance(), DELTA);
            assertEquals(0, user.getTransactionLog().size());

            verify(userService, never()).saveUser(any());
        }
    }

    @Test
    void buyFailsWithInsufficientFunds() {
        PortfolioController.BuyRequest request =
                new PortfolioController.BuyRequest("AAPL", "Apple Inc.", "shares", 200.0);

        try (MockedStatic<GetYahooPrice> mockedPrice = mockStatic(GetYahooPrice.class)) {
            mockedPrice.when(() -> GetYahooPrice.run("AAPL")).thenReturn(100.0);

            ResponseEntity<?> response = controller.buy(request, loggedInSession);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("Purchase failed — insufficient funds or invalid amount.", response.getBody());
            assertEquals(0, user.getPortfolio().getInvestments().size());
            assertEquals(10000.0, user.getCashBalance(), DELTA);
            assertEquals(0, user.getTransactionLog().size());

            verify(userService, never()).saveUser(any());
        }
    }

    @Test
    void buyFailsWhenPriceApiFails() {
        PortfolioController.BuyRequest request =
                new PortfolioController.BuyRequest("FAKE", "Fake Company", "shares", 1.0);

        try (MockedStatic<GetYahooPrice> mockedPrice = mockStatic(GetYahooPrice.class)) {
            mockedPrice.when(() -> GetYahooPrice.run("FAKE")).thenThrow(new RuntimeException("API failed"));

            ResponseEntity<?> response = controller.buy(request, loggedInSession);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertTrue(response.getBody().toString().contains("Price data is currently unavailable for FAKE"));
            assertEquals(0, user.getPortfolio().getInvestments().size());

            verify(userService, never()).saveUser(any());
        }
    }

    @Test
    void sellRequiresLogin() {
        PortfolioController.SellRequest request =
                new PortfolioController.SellRequest(1, "AAPL", "shares", 1.0);

        ResponseEntity<?> response = controller.sell(request, emptySession);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Please log in first.", response.getBody());
        verify(userService, never()).saveUser(any());
    }

    @Test
    void sellBySharesUpdatesPortfolioCashAndSavesUser() {
        Investment investment = UserTrading.purchaseStock(
                user,
                "AAPL",
                "Apple Inc.",
                100.0,
                "shares",
                10.0
        );

        try (MockedStatic<GetYahooPrice> mockedPrice = mockStatic(GetYahooPrice.class)) {
            mockedPrice.when(() -> GetYahooPrice.run("AAPL")).thenReturn(120.0);

            PortfolioController.SellRequest request =
                    new PortfolioController.SellRequest(investment.getInvestmentId(), "AAPL", "shares", 4.0);

            ResponseEntity<?> response = controller.sell(request, loggedInSession);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(6.0, investment.getShares(), DELTA);
            assertEquals(9480.0, user.getCashBalance(), DELTA);
            assertEquals(2, user.getTransactionLog().size());

            verify(userService).saveUser(user);
        }
    }

    @Test
    void sellByDollarsUpdatesPortfolioCashAndSavesUser() {
        Investment investment = UserTrading.purchaseStock(
                user,
                "MSFT",
                "Microsoft",
                100.0,
                "shares",
                10.0
        );

        try (MockedStatic<GetYahooPrice> mockedPrice = mockStatic(GetYahooPrice.class)) {
            mockedPrice.when(() -> GetYahooPrice.run("MSFT")).thenReturn(125.0);

            PortfolioController.SellRequest request =
                    new PortfolioController.SellRequest(investment.getInvestmentId(), "MSFT", "dollars", 250.0);

            ResponseEntity<?> response = controller.sell(request, loggedInSession);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(8.0, investment.getShares(), DELTA);
            assertEquals(9250.0, user.getCashBalance(), DELTA);
            assertEquals(2, user.getTransactionLog().size());

            verify(userService).saveUser(user);
        }
    }

    @Test
    void sellFailsForInvalidInvestmentId() {
        PortfolioController.SellRequest request =
                new PortfolioController.SellRequest(999, "AAPL", "shares", 1.0);

        try (MockedStatic<GetYahooPrice> mockedPrice = mockStatic(GetYahooPrice.class)) {
            mockedPrice.when(() -> GetYahooPrice.run("AAPL")).thenReturn(100.0);

            ResponseEntity<?> response = controller.sell(request, loggedInSession);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("Investment not found.", response.getBody());
            assertEquals(10000.0, user.getCashBalance(), DELTA);

            verify(userService, never()).saveUser(any());
        }
    }

    @Test
    void sellFailsWhenSellingTooManyShares() {
        Investment investment = UserTrading.purchaseStock(
                user,
                "AAPL",
                "Apple Inc.",
                100.0,
                "shares",
                5.0
        );

        try (MockedStatic<GetYahooPrice> mockedPrice = mockStatic(GetYahooPrice.class)) {
            mockedPrice.when(() -> GetYahooPrice.run("AAPL")).thenReturn(100.0);

            PortfolioController.SellRequest request =
                    new PortfolioController.SellRequest(investment.getInvestmentId(), "AAPL", "shares", 6.0);

            ResponseEntity<?> response = controller.sell(request, loggedInSession);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("Sale failed — check share count or dollar amount.", response.getBody());
            assertEquals(5.0, investment.getShares(), DELTA);
            assertEquals(9500.0, user.getCashBalance(), DELTA);

            verify(userService, never()).saveUser(any());
        }
    }

    @Test
    void sellFailsWhenPriceApiFails() {
        Investment investment = UserTrading.purchaseStock(
                user,
                "AAPL",
                "Apple Inc.",
                100.0,
                "shares",
                5.0
        );

        try (MockedStatic<GetYahooPrice> mockedPrice = mockStatic(GetYahooPrice.class)) {
            mockedPrice.when(() -> GetYahooPrice.run("AAPL")).thenThrow(new RuntimeException("API failed"));

            PortfolioController.SellRequest request =
                    new PortfolioController.SellRequest(investment.getInvestmentId(), "AAPL", "shares", 1.0);

            ResponseEntity<?> response = controller.sell(request, loggedInSession);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertTrue(response.getBody().toString().contains("Price data is currently unavailable for AAPL"));
            assertEquals(5.0, investment.getShares(), DELTA);
            assertEquals(9500.0, user.getCashBalance(), DELTA);

            verify(userService, never()).saveUser(any());
        }
    }

    @Test
    void removeRequiresLogin() {
        ResponseEntity<?> response = controller.remove(1, emptySession);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Please log in first.", response.getBody());
        verify(userService, never()).saveUser(any());
    }

    @Test
    void removeDeletesInvestmentWithoutChangingCashAndSavesUser() {
        Investment investment = UserTrading.purchaseStock(
                user,
                "AAPL",
                "Apple Inc.",
                100.0,
                "shares",
                5.0
        );

        double cashBeforeRemove = user.getCashBalance();

        ResponseEntity<?> response = controller.remove(investment.getInvestmentId(), loggedInSession);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, user.getPortfolio().getInvestments().size());
        assertEquals(cashBeforeRemove, user.getCashBalance(), DELTA);

        verify(userService).saveUser(user);
    }

    @Test
    void removeFailsForInvalidInvestmentId() {
        ResponseEntity<?> response = controller.remove(999, loggedInSession);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Investment not found.", response.getBody());
        assertEquals(0, user.getPortfolio().getInvestments().size());

        verify(userService, never()).saveUser(any());
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> asMap(Object body) {
        assertNotNull(body);
        assertTrue(body instanceof Map);
        return (Map<String, Object>) body;
    }
}