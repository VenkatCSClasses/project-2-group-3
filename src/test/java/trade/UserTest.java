package trade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    public void testConstructorAndGetters() {
        User user = new User("alice", 10000.0);

        assertEquals("alice", user.getUsername());
        assertEquals(10000.0, user.getCashBalance(), 0.0001);
        assertNotNull(user.getPortfolio());
        assertNotNull(user.getTransactionLog());
    }

    @Test
    public void testSetCashBalance() {
        User user = new User("alice", 10000.0);

        user.setCashBalance(8500.0);

        assertEquals(8500.0, user.getCashBalance(), 0.0001);
    }

    @Test
    public void testSetPortfolio() {
        User user = new User("alice", 10000.0);
        Portfolio portfolio = new Portfolio();

        Investment inv = new Investment(
                1,
                "AAPL",
                "Apple Inc.",
                "2026-04-13",
                5.0,
                200.0,
                1000.0,
                0
        );

        portfolio.addInvestment(inv);
        user.setPortfolio(portfolio);

        assertNotNull(user.getPortfolio());
        assertEquals(1, user.getPortfolio().getInvestments().size());
        assertEquals("AAPL", user.getPortfolio().getInvestments().get(0).getTicker());
    }

    @Test
    public void testSetTransactionLog() {
        User user = new User("alice", 10000.0);
        TransactionLog log = new TransactionLog();

        user.setTransactionLog(log);

        assertNotNull(user.getTransactionLog());
        assertSame(log, user.getTransactionLog());
    }
}