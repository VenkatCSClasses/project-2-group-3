package Admin;

import java.util.*;

public class Admin {
    private String adminID;
    private String password;
    
    public Admin(String adminID, String password) {
        this.adminID = adminID;
        this.password = password;
    }

    public void changePassword(String newPassword) {
        // code to change the admin password
    }

    public void createUserAccount (String userID, String password) {
        // code to create a new user account
    }

    public void deleteUserAccount (String userID) {
        // code to delete a user account
    }

    public void viewAllAccounts() {
        // code to view all user accounts
    }
    public void deleteOwnAccount() {
        // code to delete the admin's own account
    }
    public String getPassword() {
        return password;
    }
    public String getAdminID() {
        return adminID;
    }
}
