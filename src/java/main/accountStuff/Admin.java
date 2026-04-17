import java.util.*;

public class admin extends User {
    private String adminID;
    private String password;
    
    public admin(String adminID, String password) {
        this.adminID = adminID;
        this.password = password;
    }

    public void changePassword(String newPassword) {
        // code to change the admin password
    }

    public createUserAccount (String userID, String password) {
        // code to create a new user account
    }

    public deleteUserAccount (String userID) {
        // code to delete a user account
    }

    public viewAllAccounts() {
        // code to view all user accounts
    }
    public void deleteOwnAccount() {
        // code to delete the admin's own account
    }
}
