package trade;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PortfolioTest {

    @Test
    public void testPortfolioStartsEmpty() {
        Portfolio portfolio = new Portfolio();

        assertNotNull(portfolio.getInvestments());
        assertEquals(0, portfolio.getInvestments().size());
    }

    @Test
    public void testAddAndRemoveInvestment() {
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
        assertEquals(1, portfolio.getInvestments().size());

        portfolio.removeInvestment(inv);
        assertEquals(0, portfolio.getInvestments().size());
    }

    @Test
    public void testGenerateInvestmentID() {
        Portfolio portfolio = new Portfolio();

        assertEquals(1, portfolio.generateInvestmentID());
        assertEquals(2, portfolio.generateInvestmentID());
        assertEquals(3, portfolio.generateInvestmentID());
    }

    @Test
    public void testGetRealTotalValue() {
        Portfolio portfolio = new Portfolio();

        Investment a = new Investment(
                1,
                "AAPL",
                "Apple Inc.",
                "2026-04-13",
                5.0,
                200.0,
                1000.0,
                0
        );
        a.setCurrentPrice(220.0);

        Investment b = new Investment(
                2,
                "MSFT",
                "Microsoft",
                "2026-04-13",
                2.0,
                100.0,
                200.0,
                0
        );
        b.setCurrentPrice(150.0);

        portfolio.addInvestment(a);
        portfolio.addInvestment(b);

        assertEquals(1400.0, portfolio.getRealTotalValue(), 0.0001);
    }

    @Test
    public void testGetRealTotalChange() {
        Portfolio portfolio = new Portfolio();

        Investment a = new Investment(
                1,
                "AAPL",
                "Apple Inc.",
                "2026-04-13",
                5.0,
                200.0,
                1000.0,
                0
        );
        a.setCurrentPrice(220.0);

        portfolio.addInvestment(a);

        assertEquals(10.0, portfolio.getRealTotalChange(), 0.0001);
    }

    @Test
    public void testGetRealTotalValueWhenEmpty() {
        Portfolio portfolio = new Portfolio();

        assertEquals(0.0, portfolio.getRealTotalValue(), 0.0001);
    }

    @Test
    public void testGetRealTotalChangeWhenEmpty() {
        Portfolio portfolio = new Portfolio();

        assertEquals(0.0, portfolio.getRealTotalChange(), 0.0001);
    }
}