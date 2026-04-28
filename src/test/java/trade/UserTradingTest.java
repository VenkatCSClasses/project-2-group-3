package trade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTradingTest {

    private static final double DELTA = 0.0001;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("testuser", 10000.0);
    }

    @Test
    void purchaseStockBySharesCreatesNewInvestment() {
        Investment investment = UserTrading.purchaseStock(
                user,
                "AAPL",
                "Apple Inc.",
                100.0,
                "shares",
                5.0
        );

        assertNotNull(investment);
        assertEquals(1, user.getPortfolio().getInvestments().size());

        assertEquals(1, investment.getInvestmentId());
        assertEquals("AAPL", investment.getTicker());
        assertEquals("Apple Inc.", investment.getCompanyName());
        assertEquals(5.0, investment.getShares(), DELTA);
        assertEquals(100.0, investment.getPurchasePrice(), DELTA);
        assertEquals(500.0, investment.getAmountInvested(), DELTA);
        assertEquals(100.0, investment.getCurrentPrice(), DELTA);
        assertEquals(0, investment.getInvestmentType());

        assertEquals(9500.0, user.getCashBalance(), DELTA);
    }

    @Test
    void purchaseStockByDollarsCreatesNewInvestment() {
        Investment investment = UserTrading.purchaseStock(
                user,
                "MSFT",
                "Microsoft Corp.",
                200.0,
                "dollars",
                1000.0
        );

        assertNotNull(investment);
        assertEquals(1, user.getPortfolio().getInvestments().size());

        assertEquals(5.0, investment.getShares(), DELTA);
        assertEquals(200.0, investment.getPurchasePrice(), DELTA);
        assertEquals(1000.0, investment.getAmountInvested(), DELTA);
        assertEquals(9000.0, user.getCashBalance(), DELTA);
    }

    @Test
    void purchaseSameTickerCreatesSeparateInvestments() {
        Investment first = UserTrading.purchaseStock(
                user,
                "NVDA",
                "NVIDIA Corp.",
                100.0,
                "dollars",
                1000.0
        );

        Investment second = UserTrading.purchaseStock(
                user,
                "NVDA",
                "NVIDIA Corp.",
                100.0,
                "dollars",
                500.0
        );

        assertNotNull(first);
        assertNotNull(second);

        assertEquals(2, user.getPortfolio().getInvestments().size());
        assertNotEquals(first.getInvestmentId(), second.getInvestmentId());

        assertEquals(1, first.getInvestmentId());
        assertEquals(2, second.getInvestmentId());

        assertEquals(10.0, first.getShares(), DELTA);
        assertEquals(5.0, second.getShares(), DELTA);

        assertEquals(8500.0, user.getCashBalance(), DELTA);
    }

    @Test
    void purchaseStockAddsBuyTransaction() {
        UserTrading.purchaseStock(
                user,
                "AAPL",
                "Apple Inc.",
                100.0,
                "shares",
                3.0
        );

        assertEquals(1, user.getTransactionLog().size());

        Transaction transaction = user.getTransactionLog().get(0);
        assertEquals("Buy", transaction.getTransactionType());
        assertEquals("AAPL", transaction.getTicker());
        assertEquals(3.0, transaction.getShares(), DELTA);
        assertEquals(100.0, transaction.getTransactionPrice(), DELTA);
        assertEquals(300.0, transaction.getAmountInvested(), DELTA);
        assertNotNull(transaction.getTransactionDate());
    }

    @Test
    void purchaseStockFailsWithInsufficientFunds() {
        Investment investment = UserTrading.purchaseStock(
                user,
                "AAPL",
                "Apple Inc.",
                100.0,
                "shares",
                200.0
        );

        assertNull(investment);
        assertEquals(0, user.getPortfolio().getInvestments().size());
        assertEquals(10000.0, user.getCashBalance(), DELTA);
        assertEquals(0, user.getTransactionLog().size());
    }

    @Test
    void purchaseStockFailsWithZeroAmount() {
        Investment investment = UserTrading.purchaseStock(
                user,
                "AAPL",
                "Apple Inc.",
                100.0,
                "shares",
                0.0
        );

        assertNull(investment);
        assertEquals(0, user.getPortfolio().getInvestments().size());
        assertEquals(10000.0, user.getCashBalance(), DELTA);
        assertEquals(0, user.getTransactionLog().size());
    }

    @Test
    void purchaseStockFailsWithNegativeAmount() {
        Investment investment = UserTrading.purchaseStock(
                user,
                "AAPL",
                "Apple Inc.",
                100.0,
                "shares",
                -5.0
        );

        assertNull(investment);
        assertEquals(0, user.getPortfolio().getInvestments().size());
        assertEquals(10000.0, user.getCashBalance(), DELTA);
        assertEquals(0, user.getTransactionLog().size());
    }

    @Test
    void purchaseStockFailsWithZeroPrice() {
        Investment investment = UserTrading.purchaseStock(
                user,
                "AAPL",
                "Apple Inc.",
                0.0,
                "shares",
                5.0
        );

        assertNull(investment);
        assertEquals(0, user.getPortfolio().getInvestments().size());
        assertEquals(10000.0, user.getCashBalance(), DELTA);
        assertEquals(0, user.getTransactionLog().size());
    }

    @Test
    void purchaseStockFailsWithNegativePrice() {
        Investment investment = UserTrading.purchaseStock(
                user,
                "AAPL",
                "Apple Inc.",
                -100.0,
                "shares",
                5.0
        );

        assertNull(investment);
        assertEquals(0, user.getPortfolio().getInvestments().size());
        assertEquals(10000.0, user.getCashBalance(), DELTA);
        assertEquals(0, user.getTransactionLog().size());
    }

    @Test
    void purchaseStockFailsWithInvalidMethod() {
        Investment investment = UserTrading.purchaseStock(
                user,
                "AAPL",
                "Apple Inc.",
                100.0,
                "bananas",
                5.0
        );

        assertNull(investment);
        assertEquals(0, user.getPortfolio().getInvestments().size());
        assertEquals(10000.0, user.getCashBalance(), DELTA);
        assertEquals(0, user.getTransactionLog().size());
    }

    @Test
    void sellStockBySharesUpdatesCashAndShares() {
        Investment investment = UserTrading.purchaseStock(
                user,
                "AAPL",
                "Apple Inc.",
                100.0,
                "shares",
                10.0
        );

        boolean sold = UserTrading.sellStock(
                user,
                investment.getInvestmentId(),
                "AAPL",
                120.0,
                "shares",
                4.0
        );

        assertTrue(sold);
        assertEquals(1, user.getPortfolio().getInvestments().size());
        assertEquals(6.0, investment.getShares(), DELTA);
        assertEquals(9480.0, user.getCashBalance(), DELTA);
    }

    @Test
    void sellStockByDollarsUpdatesCashAndShares() {
        Investment investment = UserTrading.purchaseStock(
                user,
                "AAPL",
                "Apple Inc.",
                100.0,
                "shares",
                10.0
        );

        boolean sold = UserTrading.sellStock(
                user,
                investment.getInvestmentId(),
                "AAPL",
                125.0,
                "dollars",
                250.0
        );

        assertTrue(sold);
        assertEquals(1, user.getPortfolio().getInvestments().size());
        assertEquals(8.0, investment.getShares(), DELTA);
        assertEquals(9250.0, user.getCashBalance(), DELTA);
    }

    @Test
    void sellStockRemovesInvestmentWhenFullySold() {
        Investment investment = UserTrading.purchaseStock(
                user,
                "AAPL",
                "Apple Inc.",
                100.0,
                "shares",
                10.0
        );

        boolean sold = UserTrading.sellStock(
                user,
                investment.getInvestmentId(),
                "AAPL",
                110.0,
                "shares",
                10.0
        );

        assertTrue(sold);
        assertEquals(0, user.getPortfolio().getInvestments().size());
        assertEquals(10100.0, user.getCashBalance(), DELTA);
    }

    @Test
    void sellStockFailsWhenInvestmentDoesNotExist() {
        boolean sold = UserTrading.sellStock(
                user,
                999,
                "AAPL",
                100.0,
                "shares",
                1.0
        );

        assertFalse(sold);
        assertEquals(10000.0, user.getCashBalance(), DELTA);
        assertEquals(0, user.getTransactionLog().size());
    }

    @Test
    void sellStockFailsWhenSellingTooManyShares() {
        Investment investment = UserTrading.purchaseStock(
                user,
                "AAPL",
                "Apple Inc.",
                100.0,
                "shares",
                5.0
        );

        boolean sold = UserTrading.sellStock(
                user,
                investment.getInvestmentId(),
                "AAPL",
                100.0,
                "shares",
                6.0
        );

        assertFalse(sold);
        assertEquals(5.0, investment.getShares(), DELTA);
        assertEquals(9500.0, user.getCashBalance(), DELTA);
        assertEquals(1, user.getTransactionLog().size());
    }

    @Test
    void sellStockFailsWithInvalidMethod() {
        Investment investment = UserTrading.purchaseStock(
                user,
                "AAPL",
                "Apple Inc.",
                100.0,
                "shares",
                5.0
        );

        boolean sold = UserTrading.sellStock(
                user,
                investment.getInvestmentId(),
                "AAPL",
                100.0,
                "bananas",
                1.0
        );

        assertFalse(sold);
        assertEquals(5.0, investment.getShares(), DELTA);
        assertEquals(9500.0, user.getCashBalance(), DELTA);
        assertEquals(1, user.getTransactionLog().size());
    }

    @Test
    void sellStockFailsWithZeroAmount() {
        Investment investment = UserTrading.purchaseStock(
                user,
                "AAPL",
                "Apple Inc.",
                100.0,
                "shares",
                5.0
        );

        boolean sold = UserTrading.sellStock(
                user,
                investment.getInvestmentId(),
                "AAPL",
                100.0,
                "shares",
                0.0
        );

        assertFalse(sold);
        assertEquals(5.0, investment.getShares(), DELTA);
        assertEquals(9500.0, user.getCashBalance(), DELTA);
        assertEquals(1, user.getTransactionLog().size());
    }

    @Test
    void sellStockFailsWithNegativeAmount() {
        Investment investment = UserTrading.purchaseStock(
                user,
                "AAPL",
                "Apple Inc.",
                100.0,
                "shares",
                5.0
        );

        boolean sold = UserTrading.sellStock(
                user,
                investment.getInvestmentId(),
                "AAPL",
                100.0,
                "shares",
                -1.0
        );

        assertFalse(sold);
        assertEquals(5.0, investment.getShares(), DELTA);
        assertEquals(9500.0, user.getCashBalance(), DELTA);
        assertEquals(1, user.getTransactionLog().size());
    }

    @Test
    void sellStockAddsSellTransaction() {
        Investment investment = UserTrading.purchaseStock(
                user,
                "AAPL",
                "Apple Inc.",
                100.0,
                "shares",
                5.0
        );

        boolean sold = UserTrading.sellStock(
                user,
                investment.getInvestmentId(),
                "AAPL",
                120.0,
                "shares",
                2.0
        );

        assertTrue(sold);
        assertEquals(2, user.getTransactionLog().size());

        Transaction transaction = user.getTransactionLog().get(1);
        assertEquals("Sell", transaction.getTransactionType());
        assertEquals("AAPL", transaction.getTicker());
        assertEquals(2.0, transaction.getShares(), DELTA);
        assertEquals(120.0, transaction.getTransactionPrice(), DELTA);
        assertEquals(240.0, transaction.getAmountInvested(), DELTA);
        assertNotNull(transaction.getTransactionDate());
    }

    @Test
    void findInvestmentReturnsFirstMatchingTickerIgnoringCase() {
        Investment first = UserTrading.purchaseStock(
                user,
                "AAPL",
                "Apple Inc.",
                100.0,
                "shares",
                1.0
        );

        UserTrading.purchaseStock(
                user,
                "MSFT",
                "Microsoft Corp.",
                200.0,
                "shares",
                1.0
        );

        Investment found = UserTrading.findInvestment(user, "aapl");

        assertSame(first, found);
    }

    @Test
    void findInvestmentByIdReturnsCorrectInvestment() {
        Investment first = UserTrading.purchaseStock(
                user,
                "AAPL",
                "Apple Inc.",
                100.0,
                "shares",
                1.0
        );

        Investment second = UserTrading.purchaseStock(
                user,
                "MSFT",
                "Microsoft Corp.",
                200.0,
                "shares",
                1.0
        );

        Investment found = UserTrading.findInvestmentById(user, second.getInvestmentId());

        assertNotSame(first, found);
        assertSame(second, found);
    }

    @Test
    void findInvestmentByIdReturnsNullWhenMissing() {
        Investment found = UserTrading.findInvestmentById(user, 999);

        assertNull(found);
    }
}