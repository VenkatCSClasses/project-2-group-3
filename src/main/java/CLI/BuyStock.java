package CLI;

import apistream.*;
import storage.*;
import trade.Investment;
import trade.User;

import java.util.Scanner;

public class BuyStock {
    public static void buyStock(Scanner input, User user, PriceStream stream, String symbol, String companyName, double livePrice) {
        System.out.printf("%nBuying %s (%s) at $%.4f%n", companyName, symbol, livePrice);
        System.out.printf("Available cash: $%.2f%n", user.getCashBalance());

        System.out.println("Buy by:");
        System.out.println("1. Number of shares");
        System.out.println("2. Dollar amount");
        System.out.print("Choose (1 or 2): ");

        if (!input.hasNextInt()) { System.out.println("Invalid input."); input.nextLine(); return; }
        int methodChoice = input.nextInt();
        input.nextLine();

        if (methodChoice != 1 && methodChoice != 2) { System.out.println("Invalid choice."); return; }

        String method = (methodChoice == 1) ? "shares" : "dollars";
        System.out.print(methodChoice == 1 ? "Enter number of shares: " : "Enter dollar amount: $");

        if (!input.hasNextDouble()) { System.out.println("Invalid input."); input.nextLine(); return; }
        double amount = input.nextDouble();
        input.nextLine();

        // Re-resolve price right before executing in case stream updated it
        double execPrice = ResolvePrice.resolvePrice(symbol, stream);

        boolean success = user.purchaseStock(symbol, companyName, execPrice, method, amount);
        if (success) {
            Investment inv = user.findInvestment(symbol);
            System.out.printf("Bought %s. Position: %.4f shares @ $%.4f avg cost.%n",
                    symbol,
                    inv != null ? inv.getShares() : 0,
                    inv != null ? inv.getPurchasePrice() : execPrice);
            System.out.printf("Remaining cash: $%.2f%n", user.getCashBalance());

            // Make sure this ticker is subscribed in the stream
            stream.subscribe(symbol);
            UserDataManager.saveUser(user);
        } else {
            System.out.println("Insufficient funds. Purchase failed.");
        }
    }
}
