package system;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import CLI.PrintPortfolioSnapshot;
import storage.UserDataManager;
import trade.Investment;
import trade.User;
import trade.UserTrading;

public class PersistenceSystemTest {

    @TempDir
    Path tempDir;

    @Test
    public void testPersistenceRoundTripSystem() {
        User user = new User("trevor", 10000.0);

        Investment msftOne = UserTrading.purchaseStock(
                user,
                "MSFT",
                "Microsoft Corporation",
                100.0,
                "shares",
                6.0
        );

        Investment msftTwo = UserTrading.purchaseStock(
                user,
                "MSFT",
                "Microsoft Corporation",
                150.0,
                "shares",
                2.0
        );

        assertNotNull(msftOne);
        assertNotNull(msftTwo);

        boolean sold = UserTrading.sellStock(
                user,
                msftOne.getInvestmentId(),
                "MSFT",
                110.0,
                "shares",
                1.0
        );

        assertTrue(sold);

        msftOne.setCurrentPrice(120.0);
        msftTwo.setCurrentPrice(140.0);

        int whatIfId = user.getPortfolio().generateInvestmentID();
        Investment voo = new Investment(
                whatIfId,
                "VOO",
                "Vanguard S&P 500 ETF",
                "2025-03-01",
                4.0,
                200.0,
                800.0,
                1
        );
        voo.setCurrentPrice(210.0);
        user.getPortfolio().addInvestment(voo);

        assertEquals(9210.0, user.getCashBalance(), 0.0001);
        assertEquals(3, user.getPortfolio().getInvestments().size());
        assertEquals(3, user.getTransactionLog().size());

        UserDataManager.saveUser(user, tempDir.toString());
        User loaded = UserDataManager.loadUser("trevor", tempDir.toString());

        assertNotNull(loaded);
        assertEquals("trevor", loaded.getUsername());
        assertEquals(9210.0, loaded.getCashBalance(), 0.0001);

        assertNotNull(loaded.getPortfolio());
        assertNotNull(loaded.getPortfolio().getInvestments());
        assertEquals(3, loaded.getPortfolio().getInvestments().size());

        Investment loadedOne = loaded.getPortfolio().getInvestments().get(0);
        Investment loadedTwo = loaded.getPortfolio().getInvestments().get(1);
        Investment loadedThree = loaded.getPortfolio().getInvestments().get(2);

        assertEquals(1, loadedOne.getInvestmentId());
        assertEquals("MSFT", loadedOne.getTicker());
        assertEquals(5.0, loadedOne.getShares(), 0.0001);
        assertEquals(100.0, loadedOne.getPurchasePrice(), 0.0001);
        assertEquals(120.0, loadedOne.getCurrentPrice(), 0.0001);
        assertEquals(0, loadedOne.getInvestmentType());

        assertEquals(2, loadedTwo.getInvestmentId());
        assertEquals("MSFT", loadedTwo.getTicker());
        assertEquals(2.0, loadedTwo.getShares(), 0.0001);
        assertEquals(150.0, loadedTwo.getPurchasePrice(), 0.0001);
        assertEquals(140.0, loadedTwo.getCurrentPrice(), 0.0001);
        assertEquals(0, loadedTwo.getInvestmentType());

        assertEquals(3, loadedThree.getInvestmentId());
        assertEquals("VOO", loadedThree.getTicker());
        assertEquals(4.0, loadedThree.getShares(), 0.0001);
        assertEquals(200.0, loadedThree.getPurchasePrice(), 0.0001);
        assertEquals(210.0, loadedThree.getCurrentPrice(), 0.0001);
        assertEquals(1, loadedThree.getInvestmentType());

        assertEquals(880.0, loaded.getPortfolio().getRealTotalValue(), 0.0001);
        assertEquals(10.0, loaded.getPortfolio().getRealTotalChange(), 0.0001);

        assertEquals(840.0, loaded.getPortfolio().getWhatIfTotalValue(), 0.0001);
        assertEquals(5.0, loaded.getPortfolio().getWhatIfTotalChange(), 0.0001);

        assertEquals(3, loaded.getTransactionLog().size());
        assertEquals("Buy", loaded.getTransactionLog().get(0).getTransactionType());
        assertEquals("Buy", loaded.getTransactionLog().get(1).getTransactionType());
        assertEquals("Sell", loaded.getTransactionLog().get(2).getTransactionType());

        assertEquals(4, loaded.getPortfolio().generateInvestmentID());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try {
            System.setOut(new PrintStream(out));
            PrintPortfolioSnapshot.printPortfolioSnapshot(loaded);
        } finally {
            System.setOut(originalOut);
        }

        String output = out.toString();

        assertTrue(output.contains("Microsoft Corporation"));
        assertTrue(output.contains("Vanguard S&P 500 ETF"));
        assertTrue(output.contains("[WHAT-IF]"));
        assertTrue(output.contains("Portfolio Value (Real): $880.00 | Overall Change (Real): +10.00%"));
        assertTrue(output.contains("Portfolio Value (What-If): $840.00 | Overall Change (What-If): +5.00%"));
        assertTrue(output.contains("Cash: $9210.00"));
    }
}