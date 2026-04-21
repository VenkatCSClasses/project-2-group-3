package trade;

public class Admin {
    private String adminID;
    private String password;
    
    public Admin(String adminID, String password) {
        this.adminID = adminID;
        this.password = password;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void createUserAccount (String userID, String password) {
        // code to create a new user account
    }

    public User getUserAccount(String userID) {
        // code to get a user account by userID
        return null; // placeholder return statement
    }

    public void deleteUserAccount (String userID) {
        // code to delete a user account
    }

    public void viewAllAccounts() {
        // code to view all user accounts
    }
    public void deleteOwnAccount() {
        // code to delete the admin's own account
        this.adminID = null;
        this.password = null;
    }
    public String getPassword() {
        return password;
    }
    
    public String getAdminID() {
        return adminID;
    }
}
