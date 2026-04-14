import org.junit.jupiter.api.Test;

public class adminTests {
    @Test
    public void testChangePassword() {
        // code to test changePassword method
        Admin testAdmin = new Admin("admin1", "password123");
        testAdmin.changePassword("newPassword456");
        assertEquals("newPassword456", testAdmin.getPassword());
        assertNotEquals("password123", testAdmin.getPassword());
    }

    @Test
    public void testCreateUserAccount() {
        // code to test createUserAccount method
        Admin testAdmin = new Admin("admin1", "password123");
        User newUser = testAdmin.createUserAccount("user1", "userPassword");
        assertNotNull(newUser);
        assertEquals("user1", newUser.getUserID());
    }

    @Test
    public void testDeleteUserAccount() {
        // code to test deleteUserAccount method
        Admin testAdmin = new admin("admin1", "password123");
        User testUser = testAdmin.createUserAccount("user1", "userPassword");
        assertTrue(userList.containsKey("user1"));
        testAdmin.deleteUserAccount("user1");
        assertNull(testUser);
    }

    @Test
    public void testDeleteOwnAccount() {
        // code to test deleteOwnAccount method
        Admin testAdmin = new admin("admin1", "password123");
        testAdmin.deleteOwnAccount();
        assertNull(testAdmin);
    }
    
}
