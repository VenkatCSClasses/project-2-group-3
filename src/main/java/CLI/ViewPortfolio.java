package CLI;

import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import apistream.*;
import storage.*;
import trade.Investment;
import trade.Portfolio;
import trade.User;

public class ViewPortfolio {
    // -------------------------------------------------------------------------
    // View / manage portfolio
    // -------------------------------------------------------------------------

    public static void viewPortfolio(Scanner input, User user, PriceStream stream) {
        Portfolio portfolio = user.getPortfolio();
        ArrayList<Investment> investments = portfolio.getInvestments();

        if (investments.isEmpty()) {
            System.out.println("\nYour portfolio is empty.");
            return;
        }

        // Initial price refresh: stream first, HTTP fallback
        RefreshPortfolioPrices.refreshPortfolioPrices(portfolio, stream);

        PrintPortfolioSnapshot.printPortfolioSnapshot(user);

        System.out.println("1. Watch Live Prices  (auto-refresh every 3s)");
        System.out.println("2. Sell an investment");
        System.out.println("3. Remove an investment  (no cash impact)");
        System.out.println("4. Back to Main Menu");
        System.out.print("Choose an option: ");

        if (!input.hasNextInt()) { input.nextLine(); return; }
        int choice = input.nextInt();
        input.nextLine();

        if (choice == 1) {
            livePortfolioView(input, user, stream);
        } else if (choice == 2) {
            RemoveFromPortfolio.sellFromPortfolio(input, user, stream, investments);
        } else if (choice == 3) {
            RemoveFromPortfolio.removeFromPortfolio(input, user, investments);
        }
    }

    /** Clears the screen and continuously reprints the portfolio using stream prices. */
    public static void livePortfolioView(Scanner input, User user, PriceStream stream) {
        System.out.println("\nLive Portfolio View — updating every 3 seconds.");
        System.out.println("Press Enter at any time to stop.\n");

        Portfolio portfolio = user.getPortfolio();
        AtomicBoolean watching = new AtomicBoolean(true);

        Thread printer = new Thread(() -> {
            while (watching.get()) {
                // Apply latest stream prices
                for (Investment inv : portfolio.getInvestments()) {
                    Double sp = stream.getPrice(inv.getTicker());
                    if (sp != null) inv.setCurrentPrice(sp);
                }

                // Clear terminal and reprint
                System.out.print("\033[H\033[2J");
                System.out.flush();
                System.out.printf("=== Live Portfolio  [%s] ===%n", LocalTime.now().withNano(0));
                System.out.println("Press Enter to return to menu.\n");
                PrintPortfolioSnapshot.printPortfolioSnapshot(user);

                try {
                    Thread.sleep(3_000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        printer.setDaemon(true);
        printer.start();

        input.nextLine(); // Block until user presses Enter
        watching.set(false);
        printer.interrupt();

        // Persist the updated prices
        UserDataManager.saveUser(user);
        System.out.println("\nReturning to portfolio menu...");
    }
}
