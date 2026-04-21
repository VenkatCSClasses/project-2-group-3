package storage;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import trade.Investment;
import trade.Portfolio;
import trade.User;

public class UserDataManagerTest {
    String testDir = "data/test/users";

    @Test
    public void testSaveAndLoadUser() {
        User user = new User("kathy", 10000.0);

        Portfolio portfolio = new Portfolio();
        portfolio.addInvestment(new Investment(
                "AAPL",
                "Apple Inc.",
                "2026-04-13",
                5.0,
                200.0,
                1000.0
        ));

        user.setPortfolio(portfolio);

        UserDataManager.saveUser(user);

        User loaded = UserDataManager.loadUser("kathy");

        assertNotNull(loaded);
        assertEquals("alice", loaded.getUsername());
        assertEquals(5000.0, loaded.getCashBalance());
        assertNotNull(loaded.getPortfolio());
        assertNotNull(loaded.getPortfolio().getInvestments());
        assertEquals(1, loaded.getPortfolio().getInvestments().size());
        assertEquals("AAPL", loaded.getPortfolio().getInvestments().get(0).getTicker());
        assertEquals(5.0, loaded.getPortfolio().getInvestments().get(0).getShares());
    }
}