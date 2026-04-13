package api;

import java.util.*;

import stock.SymbolSearchResult;

public class CLI {

    public static void main(String[] args) throws Exception {
        boolean running = true;
        Scanner input = new Scanner(System.in);

        while (running) {
            System.out.print("Enter a stock symbol: ");
            String symbol = input.nextLine().toUpperCase();

            if (!symbol.matches("[A-Z]{1,5}")) {
                System.out.println("Invalid symbol format. Use 1 to 5 letters (e.g., AAPL).");
                continue;
            }

            try {
                GetPrice.run(symbol);
            } catch (Exception e) {
                System.out.println("Invalid stock symbol.");
                continue;
            }

            boolean researching = true;

            while (researching) {
                System.out.println("Quote for " + symbol + ":");
                System.out.println(GetQuote.run(symbol));
                System.out.println("---------------");

                System.out.println("1. Get Current Price "); // Intentional in case the user wants to just reference price
                System.out.println("2. Get Previous EOD Price");
                System.out.println("3. Get Quote"); // Intentional in case the user wants to reference quote again
                System.out.println("4. Get Time Series");
                System.out.println("5. Stock Symbol Search");
                System.out.println("6. Research New Stock"); 
                System.out.println("7. Exit Program");
                System.out.print("What action would you like to perform? Enter an integer 1-7: "); 

                if (!input.hasNextInt()) {
                    System.out.println("Invalid input. Please enter an integer.");
                    input.nextLine();
                    continue;
                }

                int choice = input.nextInt();
                input.nextLine();
        
                if (choice == 1) { // Get current price
                    System.out.println(GetPrice.run(symbol));
                } else if (choice == 2) { // Get end-of-day price
                    System.out.println(GetEODPrice.run(symbol));
                } else if (choice == 3) { // Get quote
                    System.out.println(GetQuote.run(symbol));
                } else if (choice == 4) {
                    List<String> intervals = List.of("1min", "5min", "15min", "30min", "45min", "1h",
                    "2h", "4h", "5h", "1day", "1week", "1month");

                    System.out.print("Choose an interval: ");
                    for (int i = 0; i < intervals.size(); i++) {
                        System.out.println((i + 1) + ". " + intervals.get(i));
                    }

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

                    String interval = intervals.get(intervalChoice - 1);

                    System.out.println(GetTimeSeries.run(symbol, interval));
                        
                } else if (choice == 5) { // Get list of similar symbols that appear for that stock symbol
                    List<SymbolSearchResult> results = SymbolSearch.run(symbol);
                    for (int i = 0; i < results.size(); i++) {
                        System.out.println(results.get(i));
                        System.out.println("------------------");
                    }
                } else if (choice == 6) {
                    researching = false;
                } else if (choice == 7) {
                    System.out.println("Closing program.");
                    researching = false;
                    running = false;
                    input.close();
                } else {
                    System.out.println("Invalid input. Please enter an integer 1 to 7.");
                }
            }
        }

    }
}