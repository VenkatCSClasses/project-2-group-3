package trade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;

import static org.junit.jupiter.api.Assertions.*;

class AuthControllerTest {

    private AuthController authController;
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        authController = new AuthController(new UserService());
        session = new MockHttpSession();
    }

    @Test
    void loginSucceedsWithValidCredentials() {
        AuthRequest request = new AuthRequest();
        request.setUsername("alice");
        request.setPassword("password1");

        ResponseEntity<String> response = authController.login(request, session);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Login successful.", response.getBody());
    }

    @Test
    void loginStoresUsernameInSession() {
        AuthRequest request = new AuthRequest();
        request.setUsername("alice");
        request.setPassword("password1");

        authController.login(request, session);

        assertEquals("alice", session.getAttribute("username"));
    }

    @Test
    void loginFailsWithWrongPassword() {
        AuthRequest request = new AuthRequest();
        request.setUsername("alice");
        request.setPassword("wrongpassword");

        ResponseEntity<String> response = authController.login(request, session);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void loginFailsWithUnknownUser() {
        AuthRequest request = new AuthRequest();
        request.setUsername("nobody");
        request.setPassword("password1");

        ResponseEntity<String> response = authController.login(request, session);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void logoutInvalidatesSession() {
        session.setAttribute("username", "alice");

        ResponseEntity<String> response = authController.logout(session);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(session.isInvalid());
    }
}
