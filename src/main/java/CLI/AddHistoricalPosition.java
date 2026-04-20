package CLI;

import storage.*;
import trade.*;
import java.util.*;

public class AddHistoricalPosition {
    // -------------------------------------------------------------------------
    // Add historical position (manual, no cash impact)
    // -------------------------------------------------------------------------

    public static void addHistoricalPosition(Scanner input, User user,
                                               String symbol, String companyName, double currentPrice) {
        System.out.print("Enter purchase date (YYYY-MM-DD): ");
        String purchaseDate = input.nextLine().trim();

        System.out.print("Enter number of shares: ");
        if (!input.hasNextDouble()) { System.out.println("Invalid input."); input.nextLine(); return; }
        double shares = input.nextDouble();
        input.nextLine();

        System.out.print("Enter purchase price per share: $");
        if (!input.hasNextDouble()) { System.out.println("Invalid input."); input.nextLine(); return; }
        double purchasePrice = input.nextDouble();
        input.nextLine();

        Investment inv = new Investment(symbol, companyName, purchaseDate,
                shares, purchasePrice, shares * purchasePrice);
        inv.setCurrentPrice(currentPrice);

        user.getPortfolio().addInvestment(inv);
        UserDataManager.saveUser(user);

        System.out.printf("Added %s (%s). Current value: $%.2f%n",
                companyName, symbol, inv.getValue());
    }
}
