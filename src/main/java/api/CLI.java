package api;

import java.io.File;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import storage.JsonFileManager;
import storage.UserData;
import stock.Quote;
import stock.SymbolSearchResult;
import trade.Investment;
import trade.Portfolio;
import trade.User;

public class CLI {

    private static final String USERS_DIR         = "data/users";
    private static final double DEFAULT_CASH = 10_000.00;

    // Hardcoded credentials — matches UserService
    private static final Map<String, String> CREDENTIALS = new HashMap<>(Map.of(
            "alice",   "password1",
            "bob",     "password2",
            "charlie", "password3"
    ));

    // -------------------------------------------------------------------------
    // Entry point
    // -------------------------------------------------------------------------

    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);

        User user = login(input);
        if (user == null) {
            System.out.println("Too many failed attempts. Exiting.");
            input.close();
            return;
        }

        // Start WebSocket price stream and subscribe to existing portfolio tickers
        PriceStream stream = new PriceStream();
        System.out.println("Connecting to live price stream...");
        stream.connect();

        List<String> tickers = user.getPortfolio().getInvestments().stream()
                .map(Investment::getTicker)
                .collect(Collectors.toList());
        if (!tickers.isEmpty()) {
            stream.subscribe(tickers);
            System.out.println("Subscribed to: " + tickers);
        }

        if (stream.isConnected()) {
            System.out.println("Live prices active.");
        } else {
            System.out.println("Could not connect to live stream. Prices will use HTTP fallback.");
        }

        boolean running = true;
        while (running) {
            System.out.println("\n=== Main Menu ===");
            System.out.printf("Logged in as: %s | Cash: $%.2f%n",
                    user.getUsername(), user.getCashBalance());
            System.out.println("1. Research a Stock");
            System.out.println("2. View / Manage Portfolio");
            System.out.println("3. Exit");
            System.out.print("Choose an option (1-3): ");

            if (!input.hasNextInt()) {
                System.out.println("Invalid input. Please enter an integer.");
                input.nextLine();
                continue;
            }

            int choice = input.nextInt();
            input.nextLine();

            if (choice == 1) {
                researchStock(input, user, stream);
            } else if (choice == 2) {
                viewPortfolio(input, user, stream);
            } else if (choice == 3) {
                saveUser(user);
                stream.close();
                System.out.println("Progress saved. Goodbye!");
                running = false;
                input.close();
            } else {
                System.out.println("Invalid input. Please enter 1, 2, or 3.");
            }
        }
    }

    // -------------------------------------------------------------------------
    // Login
    // -------------------------------------------------------------------------

    private static User login(Scanner input) {
        int attempts = 0;
        while (attempts < 3) {
            System.out.print("Username: ");
            String username = input.nextLine().trim().toLowerCase();
            System.out.print("Password: ");
            String password = input.nextLine().trim();

            String expected = CREDENTIALS.get(username);
            if (expected != null && expected.equals(password)) {
                System.out.println("Login successful. Welcome, " + username + "!");
                return loadUser(username);
            }

            attempts++;
            System.out.println("Invalid credentials. " + (3 - attempts) + " attempt(s) remaining.");
        }
        return null;
    }

    // -------------------------------------------------------------------------
    // Research menu
    // -------------------------------------------------------------------------

    private static void researchStock(Scanner input, User user, PriceStream stream) throws Exception {
        System.out.print("\nEnter a stock symbol: ");
        String symbol = input.nextLine().toUpperCase().trim();

        if (!symbol.matches("[A-Z]{1,5}")) {
            System.out.println("Invalid symbol format. Use 1-5 letters (e.g., AAPL).");
            return;
        }

        Quote quote;
        try {
            quote = GetQuote.run(symbol);
        } catch (Exception e) {
            System.out.println("Invalid stock symbol or API error.");
            return;
        }

        // Subscribe the researched ticker so stream starts filling its price
        stream.subscribe(symbol);

        boolean researching = true;
        while (researching) {
            // Resolve current price: prefer stream tick, fall back to HTTP
            double currentPrice = resolvePrice(symbol, stream);

            System.out.println("\n--- " + symbol + " ---");
            System.out.printf("Live Price : $%.4f%s%n",
                    currentPrice,
                    stream.isConnected() && stream.getPrice(symbol) != null ? " (streaming)" : " (HTTP)");
            System.out.println(quote);
            System.out.println("----------------------------------");

            System.out.println("1. Refresh Live Price");
            System.out.println("2. Get Previous EOD Price");
            System.out.println("3. Get Full Quote");
            System.out.println("4. Get Time Series");
            System.out.println("5. Stock Symbol Search");
            System.out.println("6. Buy Stock  (live price)");
            System.out.println("7. Add Historical Position  (manual)");
            System.out.println("8. Research New Stock");
            System.out.println("9. Back to Main Menu");
            System.out.print("Choose (1-9): ");

            if (!input.hasNextInt()) {
                System.out.println("Invalid input. Please enter an integer.");
                input.nextLine();
                continue;
            }

            int choice = input.nextInt();
            input.nextLine();

            if (choice == 1) {
                double price = resolvePrice(symbol, stream);
                System.out.printf("%s live price: $%.4f%s%n",
                        symbol, price,
                        stream.isConnected() && stream.getPrice(symbol) != null ? " (streaming)" : " (HTTP)");

            } else if (choice == 2) {
                System.out.println(GetEODPrice.run(symbol));

            } else if (choice == 3) {
                quote = GetQuote.run(symbol);
                System.out.println(quote);

            } else if (choice == 4) {
                List<String> intervals = List.of("1min", "5min", "15min", "30min", "45min",
                        "1h", "2h", "4h", "5h", "1day", "1week", "1month");
                System.out.println("Choose an interval:");
                for (int i = 0; i < intervals.size(); i++) {
                    System.out.println((i + 1) + ". " + intervals.get(i));
                }
                System.out.print("Enter choice: ");

                if (!input.hasNextInt()) {
                    System.out.println("Invalid input.");
                    input.nextLine();
                    continue;
                }
                int iChoice = input.nextInt();
                input.nextLine();

                if (iChoice < 1 || iChoice > intervals.size()) {
                    System.out.println("Invalid choice.");
                    continue;
                }
                System.out.println(GetTimeSeries.run(symbol, intervals.get(iChoice - 1)));

            } else if (choice == 5) {
                for (SymbolSearchResult r : SymbolSearch.run(symbol)) {
                    System.out.println(r);
                    System.out.println("------------------");
                }

            } else if (choice == 6) {
                double livePrice = resolvePrice(symbol, stream);
                buyStock(input, user, stream, symbol, quote.getName(), livePrice);

            } else if (choice == 7) {
                double livePrice = resolvePrice(symbol, stream);
                addHistoricalPosition(input, user, symbol, quote.getName(), livePrice);

            } else if (choice == 8 || choice == 9) {
                researching = false;

            } else {
                System.out.println("Invalid input. Please enter 1-9.");
            }
        }
    }

    // -------------------------------------------------------------------------
    // Buy
    // -------------------------------------------------------------------------

    private static void buyStock(Scanner input, User user, PriceStream stream,
                                  String symbol, String companyName, double livePrice) {
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
        double execPrice = resolvePrice(symbol, stream);

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
            saveUser(user);
        } else {
            System.out.println("Purchase failed — insufficient funds or invalid amount.");
        }
    }

    // -------------------------------------------------------------------------
    // Add historical position (manual, no cash impact)
    // -------------------------------------------------------------------------

    private static void addHistoricalPosition(Scanner input, User user,
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
        saveUser(user);

        System.out.printf("Added %s (%s). Current value: $%.2f%n",
                companyName, symbol, inv.getValue());
    }

    // -------------------------------------------------------------------------
    // View / manage portfolio
    // -------------------------------------------------------------------------

    private static void viewPortfolio(Scanner input, User user, PriceStream stream) {
        Portfolio portfolio = user.getPortfolio();
        ArrayList<Investment> investments = portfolio.getInvestments();

        if (investments.isEmpty()) {
            System.out.println("\nYour portfolio is empty.");
            return;
        }

        // Initial price refresh: stream first, HTTP fallback
        refreshPortfolioPrices(portfolio, stream);

        printPortfolioSnapshot(user);

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
            sellFromPortfolio(input, user, stream, investments);
        } else if (choice == 3) {
            removeFromPortfolio(input, user, investments);
        }
    }

    /** Clears the screen and continuously reprints the portfolio using stream prices. */
    private static void livePortfolioView(Scanner input, User user, PriceStream stream) {
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
                printPortfolioSnapshot(user);

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
        saveUser(user);
        System.out.println("\nReturning to portfolio menu...");
    }

    // -------------------------------------------------------------------------
    // Sell
    // -------------------------------------------------------------------------

    private static void sellFromPortfolio(Scanner input, User user, PriceStream stream,
                                           ArrayList<Investment> investments) {
        System.out.print("Enter the number of the investment to sell: ");
        if (!input.hasNextInt()) { System.out.println("Invalid input."); input.nextLine(); return; }
        int idx = input.nextInt() - 1;
        input.nextLine();

        if (idx < 0 || idx >= investments.size()) { System.out.println("Invalid selection."); return; }

        Investment inv = investments.get(idx);

        // Get freshest available price
        double livePrice = resolvePrice(inv.getTicker(), stream);
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
        double execPrice = resolvePrice(inv.getTicker(), stream);
        boolean success = user.sellStock(inv.getTicker(), execPrice, method, amount);

        if (success) {
            System.out.printf("Sale complete. Cash balance: $%.2f%n", user.getCashBalance());
            saveUser(user);
        } else {
            System.out.println("Sale failed — check share count or dollar amount.");
        }
    }

    // -------------------------------------------------------------------------
    // Remove (no cash impact)
    // -------------------------------------------------------------------------

    private static void removeFromPortfolio(Scanner input, User user, ArrayList<Investment> investments) {
        System.out.print("Enter the number of the investment to remove: ");
        if (!input.hasNextInt()) { System.out.println("Invalid input."); input.nextLine(); return; }
        int idx = input.nextInt() - 1;
        input.nextLine();

        if (idx >= 0 && idx < investments.size()) {
            Investment removed = investments.get(idx);
            user.getPortfolio().removeInvestment(removed);
            saveUser(user);
            System.out.println("Removed " + removed.getTicker() + " from portfolio.");
        } else {
            System.out.println("Invalid selection.");
        }
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    /**
     * Returns the best available price for a symbol.
     * Preference: WebSocket stream → HTTP fallback.
     * Falls back to 0.0 only if both fail (caller should handle that case).
     */
    private static double resolvePrice(String symbol, PriceStream stream) {
        Double streamPrice = stream.getPrice(symbol);
        if (streamPrice != null) return streamPrice;

        // HTTP fallback
        try {
            return GetPrice.run(symbol).getPrice();
        } catch (Exception e) {
            return 0.0;
        }
    }

    /** Applies stream prices (or HTTP fallback) to every investment in the portfolio. */
    private static void refreshPortfolioPrices(Portfolio portfolio, PriceStream stream) {
        for (Investment inv : portfolio.getInvestments()) {
            Double sp = stream.getPrice(inv.getTicker());
            if (sp != null) {
                inv.setCurrentPrice(sp);
            } else {
                try {
                    inv.setCurrentPrice(GetPrice.run(inv.getTicker()).getPrice());
                } catch (Exception ignored) {}
            }
        }
    }

    /** Prints a formatted portfolio snapshot (no I/O prompts). */
    private static void printPortfolioSnapshot(User user) {
        Portfolio portfolio = user.getPortfolio();
        ArrayList<Investment> investments = portfolio.getInvestments();

        System.out.println();
        for (int i = 0; i < investments.size(); i++) {
            Investment inv = investments.get(i);
            System.out.printf("%d. %-30s (%s)%n", i + 1, inv.getCompanyName(), inv.getTicker());
            System.out.printf("   Shares: %10.4f | Avg Cost: $%9.4f | Current: $%9.4f%n",
                    inv.getShares(), inv.getPurchasePrice(), inv.getCurrentPrice());
            System.out.printf("   Value:  $%10.2f | Change:   %+.2f%%%n%n",
                    inv.getValue(), inv.getPercentChange());
        }

        System.out.printf("Portfolio Value: $%.2f | Overall Change: %+.2f%% | Cash: $%.2f%n%n",
                portfolio.getTotalValue(), portfolio.getTotalChange(), user.getCashBalance());
    }

    // -------------------------------------------------------------------------
    // Persistence
    // -------------------------------------------------------------------------

    private static User loadUser(String username) {
        String path = USERS_DIR + "/" + username + ".json";
        try {
            UserData data = JsonFileManager.load(path, UserData.class);
            if (data != null) {
                User user = new User(data.getUsername(), data.getCashBalance());
                Portfolio p = data.getPortfolio();
                user.setPortfolio(p != null ? p : new Portfolio());
                return user;
            }
        } catch (Exception ignored) {
            // No saved file yet — start fresh
        }
        return new User(username, DEFAULT_CASH);
    }

    private static void saveUser(User user) {
        try {
            File dir = new File(USERS_DIR);
            if (!dir.exists()) dir.mkdirs();

            String path = USERS_DIR + "/" + user.getUsername() + ".json";
            UserData data = new UserData(
                    user.getUsername(),
                    CREDENTIALS.getOrDefault(user.getUsername(), ""),
                    user.getCashBalance(),
                    user.getPortfolio()
            );
            JsonFileManager.save(data, path);
        } catch (Exception e) {
            System.out.println("Warning: Could not save: " + e.getMessage());
        }
    }
}
