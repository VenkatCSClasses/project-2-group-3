import org.junit.jupiter.api.Test;

public class userListTests {

    @Test addUserTest() {
        // code to test addUser method
        UserList testUserList = new userList();
        testUserList.addUser(new User("user1", "password123"));
        assertTrue(testUserList.containsKey("user1"));
        testUserList.addUser(new User("user2", "password456"));
        assertTrue(testUserList.containsKey("user2"));
        testUserList.addUser(new Admin("admin1", "password789"));
        assertTrue(testUserList.containsKey("admin1"));

    }
    @Test removeUserTest() {
        // code to test removeUser method
        UserList testUserList = new UserList();
        testUserList.addUser(new User("user1", "password123"));
        testUserList.addUser(new Admin("admin1", "password789"));
    }
    @Test getUserTest() {
        // code to test getUser method
        UserList testUserList = new UserList();
        User testUser = new User("user1", "password123");
        testUserList.addUser(testUser);
        assertEquals(testUser, testUserList.getUser("user1"));
        Admin testAdmin = new Admin("admin1", "password789");
        testUserList.addUser(testAdmin);
        assertEquals(testAdmin, testUserList.getUser("admin1"));
    }
    
    
}
