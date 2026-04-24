package system;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import CLI.PrintPortfolioSnapshot;
import trade.Investment;
import trade.User;
import trade.UserTrading;

public class RealTradingWorkflowSystemTest {

    @Test
    public void testRealTradingWorkflowSystem() {
        User user = new User("trevor", 10000.0);

        Investment nvdaOne = UserTrading.purchaseStock(
                user,
                "NVDA",
                "NVIDIA Corporation",
                500.0,
                "shares",
                4.0
        );

        Investment nvdaTwo = UserTrading.purchaseStock(
                user,
                "NVDA",
                "NVIDIA Corporation",
                600.0,
                "dollars",
                1500.0
        );

        Investment coke = UserTrading.purchaseStock(
                user,
                "KO",
                "Coca-Cola Company",
                50.0,
                "shares",
                10.0
        );

        assertNotNull(nvdaOne);
        assertNotNull(nvdaTwo);
        assertNotNull(coke);

        assertEquals(6000.0, user.getCashBalance(), 0.0001);
        assertEquals(3, user.getPortfolio().getInvestments().size());

        assertEquals(1, nvdaOne.getInvestmentId());
        assertEquals(2, nvdaTwo.getInvestmentId());
        assertEquals(3, coke.getInvestmentId());

        assertEquals(4.0, nvdaOne.getShares(), 0.0001);
        assertEquals(2.5, nvdaTwo.getShares(), 0.0001);
        assertEquals(10.0, coke.getShares(), 0.0001);

        boolean firstSale = UserTrading.sellStock(
                user,
                nvdaTwo.getInvestmentId(),
                "NVDA",
                600.0,
                "dollars",
                600.0
        );

        boolean secondSale = UserTrading.sellStock(
                user,
                coke.getInvestmentId(),
                "KO",
                55.0,
                "shares",
                10.0
        );

        assertTrue(firstSale);
        assertTrue(secondSale);

        assertEquals(7150.0, user.getCashBalance(), 0.0001);
        assertEquals(2, user.getPortfolio().getInvestments().size());

        assertEquals(4.0, nvdaOne.getShares(), 0.0001);
        assertEquals(1.5, nvdaTwo.getShares(), 0.0001);

        assertNull(UserTrading.findInvestmentById(user, coke.getInvestmentId()));

        nvdaOne.setCurrentPrice(550.0);
        nvdaTwo.setCurrentPrice(620.0);

        assertEquals(3130.0, user.getPortfolio().getRealTotalValue(), 0.0001);
        assertEquals(7.9310344828, user.getPortfolio().getRealTotalChange(), 0.0001);

        assertEquals(5, user.getTransactionLog().size());
        assertEquals("Buy", user.getTransactionLog().get(0).getTransactionType());
        assertEquals("Buy", user.getTransactionLog().get(1).getTransactionType());
        assertEquals("Buy", user.getTransactionLog().get(2).getTransactionType());
        assertEquals("Sell", user.getTransactionLog().get(3).getTransactionType());
        assertEquals("Sell", user.getTransactionLog().get(4).getTransactionType());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try {
            System.setOut(new PrintStream(out));
            PrintPortfolioSnapshot.printPortfolioSnapshot(user);
        } finally {
            System.setOut(originalOut);
        }

        String output = out.toString();

        assertTrue(output.contains("NVIDIA Corporation"));
        assertTrue(output.contains("NVDA"));
        assertFalse(output.contains("Coca-Cola Company"));
        assertTrue(output.contains("Portfolio Value (Real): $3130.00 | Overall Change (Real): +7.93%"));
        assertTrue(output.contains("Cash: $7150.00"));
    }
}