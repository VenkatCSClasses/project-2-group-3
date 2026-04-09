package api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/*
TO-DO
Implement proper JSON input formatting in main code
    This will ensure proper tests can be written
Add integration tests
*/

class apiTest {

    @Test
    // Ensure API key is properly fetched
    void getApiKeyTest() {
        assertEquals("d355b76586984b69a33ce709af165e84", apiKey.getApiKey());
    }

    @Test
    // Ensure stock symbol is properly fetched
    void getSymbolTest() {
        assertEquals("AAPL", Symbol.getSymbol());
    }
    
}
