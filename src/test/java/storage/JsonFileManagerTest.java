package storage;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import trade.Investment;

public class JsonFileManagerTest {

    @Test
    public void testSaveAndLoadInvestmentStorage() throws Exception {
        InvestmentStorage storage = new InvestmentStorage();

        Investment investment = new Investment(
                "AAPL",
                "Apple Inc.",
                "2026-04-13",
                5.0,
                200.0,
                1000.0
        );

        storage.addInvestment(investment);

        JsonFileManager.save(storage, "data/test/test-investments.json");

        InvestmentStorage loaded =
                JsonFileManager.load("data/test/test-investments.json", InvestmentStorage.class);

        assertNotNull(loaded);
        assertNotNull(loaded.getInvestments());
        assertEquals(1, loaded.getInvestments().size());
        assertEquals("AAPL", loaded.getInvestments().get(0).getTicker());
        assertEquals(5.0, loaded.getInvestments().get(0).getShares());
    }
}