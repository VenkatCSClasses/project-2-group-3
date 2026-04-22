package CLI;

import apistream.*;
import storage.*;
import trade.Investment;
import trade.User;

import java.util.*;

public class RemoveFromPortfolio {
    // -------------------------------------------------------------------------
    // Sell
    // -------------------------------------------------------------------------

    public static void sellFromPortfolio(Scanner input, User user, PriceStream stream, ArrayList<Investment> investments) {
        System.out.print("Enter the number of the investment to sell: ");
        if (!input.hasNextInt()) { System.out.println("Invalid input."); input.nextLine(); return; }
        int idx = input.nextInt() - 1;
        input.nextLine();

        if (idx < 0 || idx >= investments.size()) { System.out.println("Invalid selection."); return; }

        Investment inv = investments.get(idx);

        // Get freshest available price
        double livePrice = ResolvePrice.resolvePrice(inv.getTicker(), stream);
        inv.setCurrentPrice(livePrice);

        System.out.printf("%nSelling %s (%s) at $%.4f%n",
                inv.getCompanyName(), inv.getTicker(), livePrice);
        System.out.printf("You own %.4f shares  (value: $%.2f)%n",
                inv.getShares(), inv.getValue());

        System.out.println("Sell by:");
        System.out.println("1. Number of shares");
        System.out.println("2. Dollar amount");
        System.out.println("3. Sell all");
        System.out.print("Choose (1-3): ");

        if (!input.hasNextInt()) { System.out.println("Invalid input."); input.nextLine(); return; }
        int methodChoice = input.nextInt();
        input.nextLine();

        String method;
        double amount;

        if (methodChoice == 3) {
            method = "shares";
            amount = inv.getShares();
        } else if (methodChoice == 1 || methodChoice == 2) {
            method = (methodChoice == 1) ? "shares" : "dollars";
            System.out.print(methodChoice == 1 ? "Shares to sell: " : "Dollar amount: $");
            if (!input.hasNextDouble()) { System.out.println("Invalid input."); input.nextLine(); return; }
            amount = input.nextDouble();
            input.nextLine();
        } else {
            System.out.println("Invalid choice.");
            return;
        }

        // Re-resolve price at execution time
        double execPrice = ResolvePrice.resolvePrice(inv.getTicker(), stream);
        boolean success = user.sellStock(inv.getTicker(), execPrice, method, amount);

        if (success) {
            System.out.printf("Sale complete. Cash balance: $%.2f%n", user.getCashBalance());
            UserDataManager.saveUser(user);
        } else {
            System.out.println("Sale failed — check share count or dollar amount.");
        }
    }

    // -------------------------------------------------------------------------
    // Remove (no cash impact)
    // -------------------------------------------------------------------------

    public static void removeFromPortfolio(Scanner input, User user, ArrayList<Investment> investments) {
        System.out.print("Enter the number of the investment to remove: ");
        if (!input.hasNextInt()) { System.out.println("Invalid input."); input.nextLine(); return; }
        int idx = input.nextInt() - 1;
        input.nextLine();

        if (idx >= 0 && idx < investments.size()) {
            Investment removed = investments.get(idx);
            user.getPortfolio().removeInvestment(removed);
            UserDataManager.saveUser(user);
            System.out.println("Removed " + removed.getTicker() + " from portfolio.");
        } else {
            System.out.println("Invalid selection.");
        }
    }
}
