package Admin;

import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AdminTests {

    @Test
    public void testAdminCreation() {
        Admin admin = new Admin("admin1", "password123");
        assertEquals("admin1", admin.getAdminID());
        assertEquals("password123", admin.getPassword());
        assertNotNull(admin);
    }
    
}
