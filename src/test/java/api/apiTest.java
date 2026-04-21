package api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ApiTest {

    //

    @Test
    // Ensure API key is properly fetched
    void getApiKeyTest() {
        assertEquals("d355b76586984b69a33ce709af165e84", ApiKey.getApiKey());
    }
    
}
