package trade;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import trade.Investment;
import trade.Portfolio;

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
        Investment inv = new Investment("AAPL", "Apple Inc.", "2026-04-13", 5.0, 200.0, 1000.0);

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
    public void testGetTotalValue() {
        Portfolio portfolio = new Portfolio();

        Investment a = new Investment("AAPL", "Apple Inc.", "2026-04-13", 5.0, 200.0, 1000.0);
        a.setCurrentPrice(220.0);

        Investment b = new Investment("MSFT", "Microsoft", "2026-04-13", 2.0, 100.0, 200.0);
        b.setCurrentPrice(150.0);

        portfolio.addInvestment(a);
        portfolio.addInvestment(b);

        assertEquals(1400.0, portfolio.getTotalValue());
    }

    @Test
    public void testGetTotalChange() {
        Portfolio portfolio = new Portfolio();

        Investment a = new Investment("AAPL", "Apple Inc.", "2026-04-13", 5.0, 200.0, 1000.0);
        a.setCurrentPrice(220.0);

        portfolio.addInvestment(a);

        assertEquals(10.0, portfolio.getTotalChange(), 0.0001);
    }

    @Test
    public void testGetTotalValueWhenEmpty() {
        Portfolio portfolio = new Portfolio();

        assertEquals(0.0, portfolio.getTotalValue());
    }

    @Test
    public void testGetTotalChangeWhenEmpty() {
        Portfolio portfolio = new Portfolio();

        assertEquals(0.0, portfolio.getTotalChange());
    }
}