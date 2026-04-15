package api;

import java.util.*;
import storage.JsonFileManager;
import stock.Price;
import stock.Quote;
import stock.SymbolSearchResult;
import trade.Investment;
import trade.Portfolio;

public class CLI {

    private static final String PORTFOLIO_FILE = "data/investments.json";

    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        Portfolio portfolio = loadPortfolio();
        boolean running = true;

        while (running) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. Research a Stock");
            System.out.println("2. View Portfolio");
            System.out.println("3. Exit");
            System.out.print("Choose an option (1-3): ");

            if (!input.hasNextInt()) {
                System.out.println("Invalid input. Please enter an integer.");
                input.nextLine();
                continue;
            }

            int mainChoice = input.nextInt();
            input.nextLine();

            if (mainChoice == 1) {
                researchStock(input, portfolio);
            } else if (mainChoice == 2) {
                viewPortfolio(input, portfolio);
            } else if (mainChoice == 3) {
                System.out.println("Closing program.");
                running = false;
                input.close();
            } else {
                System.out.println("Invalid input. Please enter 1, 2, or 3.");
            }
        }
    }

    private static void researchStock(Scanner input, Portfolio portfolio) throws Exception {
        System.out.print("\nEnter a stock symbol: ");
        String symbol = input.nextLine().toUpperCase();

        if (!symbol.matches("[A-Z]{1,5}")) {
            System.out.println("Invalid symbol format. Use 1 to 5 letters (e.g., AAPL).");
            return;
        }

        Price livePrice;
        Quote quote;
        try {
            livePrice = GetPrice.run(symbol);
            quote = GetQuote.run(symbol);
        } catch (Exception e) {
            System.out.println("Invalid stock symbol.");
            return;
        }

        boolean researching = true;
        while (researching) {
            System.out.println("\nQuote for " + symbol + ":");
            System.out.println(quote);
            System.out.println("---------------");

            System.out.println("1. Get Current Price");
            System.out.println("2. Get Previous EOD Price");
            System.out.println("3. Get Quote");
            System.out.println("4. Get Time Series");
            System.out.println("5. Stock Symbol Search");
            System.out.println("6. Add to Portfolio");
            System.out.println("7. Research New Stock");
            System.out.println("8. Back to Main Menu");
            System.out.print("What action would you like to perform? Enter an integer 1-8: ");

            if (!input.hasNextInt()) {
                System.out.println("Invalid input. Please enter an integer.");
                input.nextLine();
                continue;
            }

            int choice = input.nextInt();
            input.nextLine();

            if (choice == 1) {
                livePrice = GetPrice.run(symbol);
                System.out.println(livePrice);
            } else if (choice == 2) {
                System.out.println(GetEODPrice.run(symbol));
            } else if (choice == 3) {
                quote = GetQuote.run(symbol);
                System.out.println(quote);
            } else if (choice == 4) {
                List<String> intervals = List.of("1min", "5min", "15min", "30min", "45min", "1h",
                        "2h", "4h", "5h", "1day", "1week", "1month");

                System.out.println("Choose an interval:");
                for (int i = 0; i < intervals.size(); i++) {
                    System.out.println((i + 1) + ". " + intervals.get(i));
                }
                System.out.print("Enter choice: ");

                if (!input.hasNextInt()) {
                    System.out.println("Invalid input. Please enter an integer.");
                    input.nextLine();
                    continue;
                }

                int intervalChoice = input.nextInt();
                input.nextLine();

                if (intervalChoice < 1 || intervalChoice > intervals.size()) {
                    System.out.println("Invalid choice. Please enter an integer 1-12.");
                    continue;
                }

                System.out.println(GetTimeSeries.run(symbol, intervals.get(intervalChoice - 1)));
            } else if (choice == 5) {
                List<SymbolSearchResult> results = SymbolSearch.run(symbol);
                for (SymbolSearchResult r : results) {
                    System.out.println(r);
                    System.out.println("------------------");
                }
            } else if (choice == 6) {
                addToPortfolio(input, portfolio, symbol, quote.getName(), livePrice.getPrice());
            } else if (choice == 7) {
                researching = false;
            } else if (choice == 8) {
                researching = false;
            } else {
                System.out.println("Invalid input. Please enter an integer 1 to 8.");
            }
        }
    }

    private static void addToPortfolio(Scanner input, Portfolio portfolio,
                                        String symbol, String companyName, double currentPrice) {
        System.out.print("Enter purchase date (YYYY-MM-DD): ");
        String purchaseDate = input.nextLine().trim();

        System.out.print("Enter number of shares: ");
        if (!input.hasNextDouble()) {
            System.out.println("Invalid input.");
            input.nextLine();
            return;
        }
        double shares = input.nextDouble();
        input.nextLine();

        System.out.print("Enter purchase price per share: $");
        if (!input.hasNextDouble()) {
            System.out.println("Invalid input.");
            input.nextLine();
            return;
        }
        double purchasePrice = input.nextDouble();
        input.nextLine();

        double amountInvested = shares * purchasePrice;
        Investment inv = new Investment(symbol, companyName, purchaseDate, shares, purchasePrice, amountInvested);
        inv.setCurrentPrice(currentPrice);

        portfolio.addInvestment(inv);
        savePortfolio(portfolio);

        System.out.printf("Added %s (%s) to portfolio. Current value: $%.2f%n",
                companyName, symbol, inv.getValue());
    }

    private static void viewPortfolio(Scanner input, Portfolio portfolio) {
        ArrayList<Investment> investments = portfolio.getInvestments();

        if (investments.isEmpty()) {
            System.out.println("\nYour portfolio is empty.");
            return;
        }

        System.out.println("\nRefreshing live prices...");
        for (Investment inv : investments) {
            try {
                Price price = GetPrice.run(inv.getTicker());
                inv.setCurrentPrice(price.getPrice());
            } catch (Exception e) {
                System.out.println("Could not refresh price for " + inv.getTicker() + ", using last known price.");
            }
        }

        System.out.println("\n=== Your Portfolio ===");
        for (int i = 0; i < investments.size(); i++) {
            Investment inv = investments.get(i);
            System.out.printf("%d. %s (%s)%n", i + 1, inv.getCompanyName(), inv.getTicker());
            System.out.printf("   Shares: %.2f | Purchase Price: $%.2f | Current Price: $%.2f%n",
                    inv.getShares(), inv.getPurchasePrice(), inv.getCurrentPrice());
            System.out.printf("   Value: $%.2f | Change: %.2f%%%n%n",
                    inv.getValue(), inv.getPercentChange());
        }

        System.out.printf("Total Portfolio Value: $%.2f%n", portfolio.getTotalValue());
        System.out.printf("Total Portfolio Change: %.2f%%%n", portfolio.getTotalChange());

        System.out.println("\n1. Remove an investment");
        System.out.println("2. Back to Main Menu");
        System.out.print("Choose an option: ");

        if (!input.hasNextInt()) {
            input.nextLine();
            return;
        }

        int choice = input.nextInt();
        input.nextLine();

        if (choice == 1) {
            System.out.print("Enter the number of the investment to remove: ");
            if (!input.hasNextInt()) {
                System.out.println("Invalid input.");
                input.nextLine();
                return;
            }
            int idx = input.nextInt() - 1;
            input.nextLine();

            if (idx >= 0 && idx < investments.size()) {
                Investment removed = investments.get(idx);
                portfolio.removeInvestment(removed);
                savePortfolio(portfolio);
                System.out.println("Removed " + removed.getTicker() + " from portfolio.");
            } else {
                System.out.println("Invalid selection.");
            }
        }
    }

    private static Portfolio loadPortfolio() {
        try {
            Portfolio loaded = JsonFileManager.load(PORTFOLIO_FILE, Portfolio.class);
            if (loaded != null) return loaded;
        } catch (Exception e) {
            // File doesn't exist or is empty — start fresh
        }
        return new Portfolio();
    }

    private static void savePortfolio(Portfolio portfolio) {
        try {
            JsonFileManager.save(portfolio, PORTFOLIO_FILE);
        } catch (Exception e) {
            System.out.println("Warning: Could not save portfolio: " + e.getMessage());
        }
    }
}
