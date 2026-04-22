package CLI;

import storage.*;
import trade.Investment;
import trade.User;

import java.util.*;

import api.*;

public class AddHistoricalPosition {
    // -------------------------------------------------------------------------
    // Add historical/what-if position (manual, no cash impact)
    // -------------------------------------------------------------------------

    public static void addHistoricalPosition(Scanner input, User user, String symbol, String companyName, double currentPrice) throws Exception {
        System.out.print("Enter purchase date (YYYY-MM-DD): ");
        String purchaseDate = input.nextLine().trim();

        double price = GetOldPrice.run(symbol, purchaseDate);

        System.out.print("Enter number of shares: ");
        if (!input.hasNextDouble()) { 
                System.out.println("Invalid input. Please enter an integer."); 
                input.nextLine(); 
                return; 
        }
        double shares = input.nextDouble();
        input.nextLine();

        // ?
        Investment inv = new Investment(symbol, companyName, purchaseDate, shares, price, shares * price, 1);
        inv.setCurrentPrice(currentPrice);

        user.getPortfolio().addInvestment(inv);
        UserDataManager.saveUser(user);

        System.out.printf("Added %s (%s). Current value: $%.2f%n", companyName, symbol, inv.getValue());
    }
}
