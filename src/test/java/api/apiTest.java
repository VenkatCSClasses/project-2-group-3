package api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ApiTest {

    @Test
    // Ensure API key is properly fetched
    void getApiKeyTest() {
        assertEquals("912887cdb07d48adb5c6eabde5c07494", ApiKey.getApiKey());
    }
    
}
