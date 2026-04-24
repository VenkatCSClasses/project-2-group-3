package CLI;

import java.util.Scanner;

public class DefinitionsMenu {

    public static void definitionsMenu(Scanner input) {
        boolean running = true;

        while (running) {
            System.out.println("=== Definitions Menu ===");
            System.out.println("1. Stock");
            System.out.println("2. Ticker Symbol");
            System.out.println("3. Company Name");
            System.out.println("4. Live Price");
            System.out.println("5. End-of-Day Price");
            System.out.println("6. Quote");
            System.out.println("7. Time Series");
            System.out.println("8. Symbol Search");
            System.out.println("9. Symbol");
            System.out.println("10. Name");
            System.out.println("11. Exchange");
            System.out.println("12. Exchange Timezone");
            System.out.println("13. MIC Code");
            System.out.println("14. Currency");
            System.out.println("15. Instrument Type");
            System.out.println("16. Datetime");
            System.out.println("17. Timestamp");
            System.out.println("18. Interval");
            System.out.println("19. Open");
            System.out.println("20. High");
            System.out.println("21. Low");
            System.out.println("22. Close");
            System.out.println("23. Volume");
            System.out.println("24. Previous Close");
            System.out.println("25. Change");
            System.out.println("26. Percent Change");
            System.out.println("27. 52-Week Low");
            System.out.println("28. 52-Week High");
            System.out.println("29. 52-Week Low Change");
            System.out.println("30. 52-Week High Change");
            System.out.println("31. 52-Week Low Change Percent");
            System.out.println("32. 52-Week High Change Percent");
            System.out.println("33. 52-Week Range");
            System.out.println("34. Back to Main Menu");
            System.out.print("Choose an option: ");

            if (!input.hasNextInt()) {
                System.out.println("Invalid input. Please enter an integer 1-34.");
                input.nextLine();
                continue;
            }

            int choice = input.nextInt();
            input.nextLine();

            if (choice == 1) {
                System.out.println("Stock: Share of ownership in a company that can be bought and sold on an exchange.");
            } else if (choice == 2) {
                System.out.println("Ticker Symbol: Short code used to identify a stock, such as AAPL.");
            } else if (choice == 3) {
                System.out.println("Company Name: Full name of the company tied to a stock symbol.");
            } else if (choice == 4) {
                System.out.println("Live Price: Most current available price of the stock.");
            } else if (choice == 5) {
                System.out.println("End-of-Day Price: Final recorded price of the stock at the end of a trading day.");
            } else if (choice == 6) {
                System.out.println("Quote: Current stock summary including price data, volume, daily movement, and 52-week data.");
            } else if (choice == 7) {
                System.out.println("Time Series: List of stock data points across time intervals.");
            } else if (choice == 8) {
                System.out.println("Symbol Search: Search used to find stocks by symbol or company name.");
            } else if (choice == 9) {
                System.out.println("Symbol: Stock code used to identify the company in the market.");
            } else if (choice == 10) {
                System.out.println("Name: Full company or instrument name.");
            } else if (choice == 11) {
                System.out.println("Exchange: Market where the stock is traded, such as NASDAQ or NYSE.");
            } else if (choice == 12) {
                System.out.println("Exchange Timezone: Time zone used by the exchange for its market data.");
            } else if (choice == 13) {
                System.out.println("MIC Code: Standard code used to identify a specific exchange.");
            } else if (choice == 14) {
                System.out.println("Currency: Money unit the stock price is shown in, such as USD.");
            } else if (choice == 15) {
                System.out.println("Instrument Type: Type of financial asset, such as stock, ETF, or index.");
            } else if (choice == 16) {
                System.out.println("Datetime: Date and time attached to the stock data.");
            } else if (choice == 17) {
                System.out.println("Timestamp: Datetime stored as a numeric Unix time value.");
            } else if (choice == 18) {
                System.out.println("Interval: Time unit used in time series data, such as 1min, 1day, or 1week.");
            } else if (choice == 19) {
                System.out.println("Open: Price at the beginning of the trading period.");
            } else if (choice == 20) {
                System.out.println("High: Highest price reached during the trading period.");
            } else if (choice == 21) {
                System.out.println("Low: Lowest price reached during the trading period.");
            } else if (choice == 22) {
                System.out.println("Close: Price at the end of the trading period.");
            } else if (choice == 23) {
                System.out.println("Volume: Number of shares traded during the trading period.");
            } else if (choice == 24) {
                System.out.println("Previous Close: Closing price from the previous trading day.");
            } else if (choice == 25) {
                System.out.println("Change: Difference between the current price and the previous close.");
            } else if (choice == 26) {
                System.out.println("Percent Change: Percentage difference between the current price and the previous close.");
            } else if (choice == 27) {
                System.out.println("52-Week Low: Lowest price reached during the last 52 weeks.");
            } else if (choice == 28) {
                System.out.println("52-Week High: Highest price reached during the last 52 weeks.");
            } else if (choice == 29) {
                System.out.println("52-Week Low Change: Difference between the current price and the 52-week low.");
            } else if (choice == 30) {
                System.out.println("52-Week High Change: Difference between the current price and the 52-week high.");
            } else if (choice == 31) {
                System.out.println("52-Week Low Change Percent: Percentage difference between the current price and the 52-week low.");
            } else if (choice == 32) {
                System.out.println("52-Week High Change Percent: Percentage difference between the current price and the 52-week high.");
            } else if (choice == 33) {
                System.out.println("52-Week Range: Lowest and highest prices reached during the last 52 weeks.");
            } else if (choice == 34) {
                running = false;
            } else {
                System.out.println("Invalid input. Please enter an integer 1-34.");
            }
        }
    }
}