package trade;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class InvestmentTest {

    @Test
    public void testConstructorAndGetters() {
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

        assertEquals(1, inv.getInvestmentId());
        assertEquals("AAPL", inv.getTicker());
        assertEquals("Apple Inc.", inv.getCompanyName());
        assertEquals("2026-04-13", inv.getPurchaseDate());
        assertEquals(5.0, inv.getShares());
        assertEquals(200.0, inv.getPurchasePrice());
        assertEquals(1000.0, inv.getAmountInvested());
        assertEquals(0, inv.getInvestmentType());
        assertEquals(200.0, inv.getCurrentPrice());
    }

    @Test
    public void testSetCurrentPrice() {
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

        inv.setCurrentPrice(220.0);

        assertEquals(220.0, inv.getCurrentPrice());
    }

    @Test
    public void testGetValue() {
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

        inv.setCurrentPrice(220.0);

        assertEquals(1100.0, inv.getValue());
    }

    @Test
    public void testGetPercentChange() {
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

        inv.setCurrentPrice(220.0);

        assertEquals(10.0, inv.getPercentChange(), 0.0001);
    }

    @Test
    public void testAddShares() {
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

        inv.addShares(5.0, 300.0);

        assertEquals(10.0, inv.getShares(), 0.0001);
        assertEquals(250.0, inv.getPurchasePrice(), 0.0001);
        assertEquals(2500.0, inv.getAmountInvested(), 0.0001);
    }

    @Test
    public void testRemoveShares() {
        Investment inv = new Investment(
                1,
                "AAPL",
                "Apple Inc.",
                "2026-04-13",
                10.0,
                200.0,
                2000.0,
                0
        );

        inv.removeShares(4.0);

        assertEquals(6.0, inv.getShares(), 0.0001);
        assertEquals(1200.0, inv.getAmountInvested(), 0.0001);
    }

    @Test
    public void testRemoveAllShares() {
        Investment inv = new Investment(
                1,
                "AAPL",
                "Apple Inc.",
                "2026-04-13",
                10.0,
                200.0,
                2000.0,
                0
        );

        inv.removeShares(10.0);

        assertEquals(0.0, inv.getShares(), 0.0001);
        assertEquals(0.0, inv.getAmountInvested(), 0.0001);
    }
}