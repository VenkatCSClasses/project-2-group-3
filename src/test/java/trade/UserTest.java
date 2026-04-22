package trade;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

// current fails

public class UserTest {

    @Test
    public void testPurchaseStockByShares() {
        User user = new User("alice", 10000.0);

        boolean success = user.purchaseStock("AAPL", "Apple Inc.", 200.0, "shares", 5.0);

        assertTrue(success);
        assertEquals(9000.0, user.getCashBalance(), 0.0001);
        assertNotNull(user.findInvestment("AAPL"));
        assertEquals(5.0, user.findInvestment("AAPL").getShares(), 0.0001);
    }

    @Test
    public void testPurchaseStockByDollars() {
        User user = new User("alice", 10000.0);

        boolean success = user.purchaseStock("AAPL", "Apple Inc.", 200.0, "dollars", 1000.0);

        assertTrue(success);
        assertEquals(9000.0, user.getCashBalance(), 0.0001);
        assertNotNull(user.findInvestment("AAPL"));
        assertEquals(5.0, user.findInvestment("AAPL").getShares(), 0.0001);
    }

    @Test
    public void testPurchaseFailsForInsufficientFunds() {
        User user = new User("alice", 500.0);

        boolean success = user.purchaseStock("AAPL", "Apple Inc.", 200.0, "shares", 5.0);

        assertFalse(success);
        assertEquals(500.0, user.getCashBalance(), 0.0001);
        assertNull(user.findInvestment("AAPL"));
    }

    @Test
    public void testPurchaseFailsForInvalidMode() {
        User user = new User("alice", 10000.0);

        boolean success = user.purchaseStock("AAPL", "Apple Inc.", 200.0, "bananas", 5.0);

        assertFalse(success);
        assertEquals(10000.0, user.getCashBalance(), 0.0001);
        assertNull(user.findInvestment("AAPL"));
    }

    @Test
    public void testSellStockByShares() {
        User user = new User("alice", 10000.0);
        user.purchaseStock("AAPL", "Apple Inc.", 200.0, "shares", 5.0);

        boolean success = user.sellStock("AAPL", 250.0, "shares", 2.0);

        assertTrue(success);
        assertEquals(9500.0, user.getCashBalance(), 0.0001);
        assertNotNull(user.findInvestment("AAPL"));
        assertEquals(3.0, user.findInvestment("AAPL").getShares(), 0.0001);
    }

    @Test
    public void testSellStockByDollars() {
        User user = new User("alice", 10000.0);
        user.purchaseStock("AAPL", "Apple Inc.", 200.0, "shares", 5.0);

        boolean success = user.sellStock("AAPL", 250.0, "dollars", 500.0);

        assertTrue(success);
        assertEquals(9500.0, user.getCashBalance(), 0.0001);
        assertNotNull(user.findInvestment("AAPL"));
        assertEquals(3.0, user.findInvestment("AAPL").getShares(), 0.0001);
    }

    @Test
    public void testSellFailsWhenInvestmentNotFound() {
        User user = new User("alice", 10000.0);

        boolean success = user.sellStock("AAPL", 250.0, "shares", 1.0);

        assertFalse(success);
    }

    @Test
    public void testSellFailsWhenTooManySharesRequested() {
        User user = new User("alice", 10000.0);
        user.purchaseStock("AAPL", "Apple Inc.", 200.0, "shares", 5.0);

        boolean success = user.sellStock("AAPL", 250.0, "shares", 10.0);

        assertFalse(success);
        assertEquals(9000.0, user.getCashBalance(), 0.0001);
        assertNotNull(user.findInvestment("AAPL"));
        assertEquals(5.0, user.findInvestment("AAPL").getShares(), 0.0001);
    }

    @Test
    public void testSellFailsForInvalidMode() {
        User user = new User("alice", 10000.0);
        user.purchaseStock("AAPL", "Apple Inc.", 200.0, "shares", 5.0);

        boolean success = user.sellStock("AAPL", 250.0, "bananas", 2.0);

        assertFalse(success);
        assertEquals(9000.0, user.getCashBalance(), 0.0001);
        assertNotNull(user.findInvestment("AAPL"));
        assertEquals(5.0, user.findInvestment("AAPL").getShares(), 0.0001);
    }

    @Test
    public void testSellRemovesInvestmentWhenSharesReachZero() {
        User user = new User("alice", 10000.0);
        user.purchaseStock("AAPL", "Apple Inc.", 200.0, "shares", 5.0);

        boolean success = user.sellStock("AAPL", 250.0, "shares", 5.0);

        assertTrue(success);
        assertNull(user.findInvestment("AAPL"));
    }

    @Test
    public void testFindInvestmentIgnoresCase() {
        User user = new User("alice", 10000.0);
        user.purchaseStock("AAPL", "Apple Inc.", 200.0, "shares", 5.0);

        assertNotNull(user.findInvestment("aapl"));
    }
}