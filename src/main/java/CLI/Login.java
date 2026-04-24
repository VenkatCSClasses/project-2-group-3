package CLI;

import java.util.*;

import storage.*;
import trade.User;

public class Login {

    public static User startingScreen(Scanner input) throws Exception {
        boolean starting = true;
        
        while (starting) {
            System.out.println("=== Welcome! ===");
            System.out.println("1. Login");
            System.out.println("2. Create New Account");
            System.out.print("Choose (1-2): ");

            if (!input.hasNextInt()) {
                System.out.println("Invalid input. Please enter an integer 1-2.");
                input.nextLine();
                continue;
            }

            int choice = input.nextInt();
            input.nextLine();

            if (choice == 1) {
                User user = login(input);
                return user;
            } else if (choice == 2) {
                User user = createAccount(input);
                return user;
            }
        }
        return null;
    }

    public static User login(Scanner input) throws Exception {
        int attempts = 0;
        while (attempts < 3) {
            System.out.print("Username: ");
            String username = input.nextLine().trim().toLowerCase();
            System.out.print("Password: ");
            String password = input.nextLine().trim();

            if (CredentialsDataManager.validateLogin(username, password)) {
                System.out.println("Login successful. Welcome, " + username + "!");
                return UserDataManager.loadUser(username);
            } else {
                attempts++;
                System.out.println("Invalid credentials. " + (3 - attempts) + " attempt(s) remaining.");
            }
        }
        return null;
    }

    public static User createAccount(Scanner input) throws Exception {
        boolean running = true;

        while (running) {
            System.out.print("Enter your username - no spaces allowed: ");
            String username = input.nextLine().trim().toLowerCase();
            System.out.print("Enter your password: "); // todo add forbidden symbols
            String password = input.nextLine().trim();

            if(!CredentialsDataManager.createAccount(username, password)) {
                System.out.println("The username " + username + "is already taken. Please choose another username.");
            } else {
                System.out.println("Welcome, " + username + "!");
                return UserDataManager.loadUser(username);
            }
        }
        return null;
    }
}
