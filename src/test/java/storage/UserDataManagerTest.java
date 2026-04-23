package storage;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.Test;

import trade.Investment;
import trade.Portfolio;
import trade.User;

public class UserDataManagerTest {

    private final String testDir = "data/test";

    @Test
    public void testSaveAndLoadUser() {
        new File(testDir).mkdirs();

        User user = new User("bobby", 10000.0);

        Portfolio portfolio = new Portfolio();
        portfolio.addInvestment(new Investment(
                1,
                "AAPL",
                "Apple Inc.",
                "2026-04-13",
                5.0,
                200.0,
                1000.0,
                0
        ));

        user.setPortfolio(portfolio);

        UserDataManager.saveUser(user, testDir);
        User loaded = UserDataManager.loadUser("bobby", testDir);

        assertNotNull(loaded);
        assertEquals("bobby", loaded.getUsername());
        assertEquals(10000.0, loaded.getCashBalance());
        assertNotNull(loaded.getPortfolio());
        assertNotNull(loaded.getPortfolio().getInvestments());
        assertEquals(1, loaded.getPortfolio().getInvestments().size());

        Investment loadedInvestment = loaded.getPortfolio().getInvestments().get(0);
        assertEquals(1, loadedInvestment.getInvestmentId());
        assertEquals("AAPL", loadedInvestment.getTicker());
        assertEquals("Apple Inc.", loadedInvestment.getCompanyName());
        assertEquals("2026-04-13", loadedInvestment.getPurchaseDate());
        assertEquals(5.0, loadedInvestment.getShares());
        assertEquals(200.0, loadedInvestment.getPurchasePrice());
        assertEquals(1000.0, loadedInvestment.getAmountInvested());
        assertEquals(0, loadedInvestment.getInvestmentType());
    }
}