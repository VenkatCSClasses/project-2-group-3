package system;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import CLI.PrintPortfolioSnapshot;
import trade.Investment;
import trade.User;
import trade.UserTrading;

public class WhatIfPortfolioWorkflowSystemTest {

    @Test
    public void testWhatIfPortfolioWorkflowSystem() {
        User user = new User("trevor", 10000.0);

        Investment amzn = UserTrading.purchaseStock(
                user,
                "AMZN",
                "Amazon.com Inc.",
                120.0,
                "dollars",
                1200.0
        );

        Investment xom = UserTrading.purchaseStock(
                user,
                "XOM",
                "Exxon Mobil Corporation",
                100.0,
                "shares",
                8.0
        );

        assertNotNull(amzn);
        assertNotNull(xom);

        amzn.setCurrentPrice(132.0);
        xom.setCurrentPrice(90.0);

        int whatIfOneId = user.getPortfolio().generateInvestmentID();
        Investment spy = new Investment(
                whatIfOneId,
                "SPY",
                "SPDR S&P 500 ETF Trust",
                "2024-01-10",
                3.0,
                400.0,
                1200.0,
                1
        );
        spy.setCurrentPrice(450.0);

        int whatIfTwoId = user.getPortfolio().generateInvestmentID();
        Investment qqq = new Investment(
                whatIfTwoId,
                "QQQ",
                "Invesco QQQ Trust",
                "2024-02-15",
                2.0,
                300.0,
                600.0,
                1
        );
        qqq.setCurrentPrice(270.0);

        user.getPortfolio().addInvestment(spy);
        user.getPortfolio().addInvestment(qqq);

        assertEquals(8000.0, user.getCashBalance(), 0.0001);
        assertEquals(4, user.getPortfolio().getInvestments().size());

        assertEquals(2040.0, user.getPortfolio().getRealTotalValue(), 0.0001);
        assertEquals(2.0, user.getPortfolio().getRealTotalChange(), 0.0001);

        assertEquals(1890.0, user.getPortfolio().getWhatIfTotalValue(), 0.0001);
        assertEquals(5.0, user.getPortfolio().getWhatIfTotalChange(), 0.0001);

        assertEquals(2, user.getTransactionLog().size());
        assertEquals("Buy", user.getTransactionLog().get(0).getTransactionType());
        assertEquals("Buy", user.getTransactionLog().get(1).getTransactionType());

        ByteArrayOutputStream beforeOut = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try {
            System.setOut(new PrintStream(beforeOut));
            PrintPortfolioSnapshot.printPortfolioSnapshot(user);
        } finally {
            System.setOut(originalOut);
        }

        String beforeOutput = beforeOut.toString();

        assertTrue(beforeOutput.contains("Amazon.com Inc."));
        assertTrue(beforeOutput.contains("Exxon Mobil Corporation"));
        assertTrue(beforeOutput.contains("SPDR S&P 500 ETF Trust"));
        assertTrue(beforeOutput.contains("Invesco QQQ Trust"));
        assertTrue(beforeOutput.contains("[WHAT-IF]"));
        assertTrue(beforeOutput.contains("Portfolio Value (Real): $2040.00 | Overall Change (Real): +2.00%"));
        assertTrue(beforeOutput.contains("Portfolio Value (What-If): $1890.00 | Overall Change (What-If): +5.00%"));
        assertTrue(beforeOutput.contains("Cash: $8000.00"));

        user.getPortfolio().removeInvestment(qqq);

        assertEquals(8000.0, user.getCashBalance(), 0.0001);
        assertEquals(3, user.getPortfolio().getInvestments().size());
        assertEquals(2, user.getTransactionLog().size());

        assertEquals(1350.0, user.getPortfolio().getWhatIfTotalValue(), 0.0001);
        assertEquals(12.5, user.getPortfolio().getWhatIfTotalChange(), 0.0001);

        ByteArrayOutputStream afterOut = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(afterOut));
            PrintPortfolioSnapshot.printPortfolioSnapshot(user);
        } finally {
            System.setOut(originalOut);
        }

        String afterOutput = afterOut.toString();

        assertTrue(afterOutput.contains("SPDR S&P 500 ETF Trust"));
        assertFalse(afterOutput.contains("Invesco QQQ Trust"));
        assertTrue(afterOutput.contains("Portfolio Value (What-If): $1350.00 | Overall Change (What-If): +12.50%"));
        assertTrue(afterOutput.contains("Cash: $8000.00"));
    }
}