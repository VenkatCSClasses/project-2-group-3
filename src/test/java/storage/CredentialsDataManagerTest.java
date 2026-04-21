package storage;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class CredentialsDataManagerTest {

    @Test
    public void testLoadCredentials() throws Exception {
        CredentialsDataManager.setCredentialsFile("data/test/test-credentials.json");

        Map<String, String> credentials = CredentialsDataManager.loadCredentials();

        assertNotNull(credentials);
        assertTrue(credentials.containsKey("billybob"));
        assertEquals("1234", credentials.get("billybob"));
    }

    @Test
    public void testCreateAccount() throws Exception {
        boolean created = CredentialsDataManager.createAccount("joe", "5678");
        Map<String, String> updated = CredentialsDataManager.loadCredentials();

        assertTrue(created);
        assertTrue(updated.containsKey("joe"));
        assertEquals("5678", updated.get("joe"));
    }

    @Test
    public void testCreateAccountFail() throws Exception {
        boolean created = CredentialsDataManager.createAccount("billybob", "hi");
        Map<String, String> updated = CredentialsDataManager.loadCredentials();

        assertFalse(created);
        assertEquals("1234", updated.get("billybob"));
    }

    @Test
    public void testValidateLogin() throws Exception {
        boolean valid = CredentialsDataManager.validateLogin("billybob", "1234");
        assertTrue(valid);
    }

    @Test
    public void testValidateLoginFail() throws Exception {
        boolean valid = CredentialsDataManager.validateLogin("billybob", "1235");
        assertFalse(valid);
    }
}