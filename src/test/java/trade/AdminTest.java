package trade;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AdminTest {

    @Test
    public void testAdminCreation() {
        Admin admin = new Admin("admin1", "password123");
        assertEquals("admin1", admin.getAdminID());
        assertEquals("password123", admin.getPassword());
        assertNotNull(admin);
    }
    @Test
    public void testChangePassword() {
        Admin admin = new Admin("admin1", "password123");
        admin.changePassword("newpassword456");
        assertEquals("newpassword456", admin.getPassword());
    }

    @Test
    public void testCreateUserAccount() {
        Admin admin = new Admin("admin1", "password123");
        admin.createUserAccount("user1", "userpassword");
    }

    @Test
    public void testDeleteUserAccount() {
        Admin admin = new Admin("admin1", "password123");
        admin.createUserAccount("user1", "userpassword");
        admin.deleteUserAccount("user1");
        assertNull(admin.getUserAccount("user1"));
    }

    @Test
    public void testgetAdminID() {
        Admin admin = new Admin("admin1", "password123");
        assertEquals("admin1", admin.getAdminID());
    }

    @Test void testgetPassword() {
        Admin admin = new Admin("admin1", "password123");
        assertEquals("password123", admin.getPassword());
    }

    @Test
    public void testDeleteOwnAccount() {
        Admin admin = new Admin("admin1", "password123");
        admin.deleteOwnAccount();
        assertNull(admin.getAdminID());
        assertNull(admin.getPassword());
    }

}
