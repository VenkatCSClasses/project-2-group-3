package storage;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class UserDataJsonTest {

    @Test
    public void testLoadUser1Json() throws Exception {
        UserData data = JsonFileManager.load("data/test/test-user1.json", UserData.class);

        assertNotNull(data);
        assertEquals("phillip", data.getUsername());
        assertEquals("1234", data.getPassword());
        assertEquals(10000.0, data.getCashBalance());
        assertNotNull(data.getPortfolio());
        assertNotNull(data.getPortfolio().getInvestments());
        assertEquals(0, data.getPortfolio().getInvestments().size());
    }

    @Test
    public void testLoadUser2Json() throws Exception {
        UserData data = JsonFileManager.load("data/test/test-user2.json", UserData.class);

        assertNotNull(data);
        assertEquals("susan", data.getUsername());
        assertEquals("1234", data.getPassword());
        assertEquals(1279.83, data.getCashBalance());
        assertNotNull(data.getPortfolio());
        assertNotNull(data.getPortfolio().getInvestments());
        assertEquals(2, data.getPortfolio().getInvestments().size());

        assertEquals("AMZN", data.getPortfolio().getInvestments().get(0).getTicker());
        assertEquals(20.0, data.getPortfolio().getInvestments().get(0).getShares());

        assertEquals("DXCM", data.getPortfolio().getInvestments().get(1).getTicker());
        assertEquals(10.0, data.getPortfolio().getInvestments().get(1).getShares());
    }
}