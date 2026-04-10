package trade;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    void loginSucceedsWithValidCredentials() {
        assertTrue(userService.login("alice", "password1"));
    }

    @Test
    void loginFailsWithWrongPassword() {
        assertFalse(userService.login("alice", "wrongpassword"));
    }

    @Test
    void loginFailsWithUnknownUsername() {
        assertFalse(userService.login("unknown", "password1"));
    }

    @Test
    void loginIsCaseSensitiveForUsername() {
        assertFalse(userService.login("Alice", "password1"));
    }

    @Test
    void allPrePopulatedUsersCanLogin() {
        assertTrue(userService.login("alice", "password1"));
        assertTrue(userService.login("bob", "password2"));
        assertTrue(userService.login("charlie", "password3"));
    }
}
