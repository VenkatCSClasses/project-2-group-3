package CLI;

import java.util.*;
import java.util.stream.Collectors;

import api.*;
import apistream.*;
import storage.*;
import trade.Investment;
import trade.User;
import stock.*;

public class CLI {
    // -------------------------------------------------------------------------
    // Entry point
    // -------------------------------------------------------------------------

    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        User user = Login.startingScreen(input);

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
            System.out.println("3. Definitions Menu");
            System.out.println("4. Assistance Menu");
            System.out.println("5. Exit");
            System.out.print("Choose an option (1-5): ");

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
                ViewPortfolio.viewPortfolio(input, user, stream);
            } else if (choice == 3) {
                DefinitionsMenu.definitionsMenu(input);
            } else if (choice == 4) {
              System.out.println("WIP");
            } else if (choice == 5) {
                UserDataManager.saveUser(user);
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
            System.out.println("API error: " + e.getMessage());
            return;
        }

        System.out.println("\n--- Quote for " + symbol + " ---");
        System.out.println(quote);
        System.out.println("----------------------------------");
        researchMenu(input, user, symbol, quote, stream);
    }

    private static void researchMenu(Scanner input, User user, String symbol, Quote quote, PriceStream stream) throws Exception {
        // Subscribe the researched ticker so stream starts filling its price
        stream.subscribe(symbol);

        boolean researching = true;
        while (researching) {
            // Resolve current price: prefer stream tick, fall back to HTTP
            System.out.println("1. Refresh Live Price");
            System.out.println("2. Get Previous EOD Price");
            System.out.println("3. Get Full Quote");
            System.out.println("4. Get Time Series");
            System.out.println("5. Similar Stocks");
            System.out.println("6. Buy Stock (live price)");
            System.out.println("7. Add Historical Position (what-if)");
            System.out.println("8. Research New Stock");
            System.out.println("9. Back to Main Menu");
            System.out.print("Choose (1-9): ");

            if (!input.hasNextInt()) {
                System.out.println("Invalid input. Please enter an integer 1-9.");
                input.nextLine();
                continue;
            }

            int choice = input.nextInt();
            input.nextLine();

            if (choice == 1) {
                double price = ResolvePrice.resolvePrice(symbol, stream);
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
                double livePrice = ResolvePrice.resolvePrice(symbol, stream);
                BuyStock.buyStock(input, user, stream, symbol, quote.getName(), livePrice);

            } else if (choice == 7) {
                double livePrice = ResolvePrice.resolvePrice(symbol, stream);
                AddHistoricalPosition.addHistoricalPosition(input, user, symbol, quote.getName(), livePrice);

            } else if (choice == 8) {
                researchStock(input, user, stream);
            } else if (choice == 9) {
                researching = false;

            } else {
                System.out.println("Invalid input. Please enter 1-9.");
            }
        }
    }
}
