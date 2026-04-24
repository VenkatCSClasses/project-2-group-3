package CLI;
import java.util.Scanner;

public class AssistanceMenu {
    public static void assistanceMenu(Scanner input) {
        boolean running = true;

        while (running) {
            System.out.println("=== Assistance Menu ===");
            System.out.println("1. Overview");
            System.out.println("2. Stock Research");
            System.out.println("3. Purchasing Stocks");
            System.out.println("4. Historical / What-If Purchases");
            System.out.println("5. Portfolio Management");
            System.out.println("6. Selling Stocks");
            System.out.println("7. Transaction Log");
            System.out.println("8. Definitions and Assistance Menu");
            System.out.println("9. Account Specifications");
            System.out.println("10. Account Management");
            System.out.println("11. Limitations");
            System.out.println("12. Exit Assistance Menu");

            if (!input.hasNextInt()) {
                System.out.println("Invalid input. Please enter an integer 1-12.");
                input.nextLine();
                continue;
            }

            int choice = input.nextInt();
            input.nextLine();

            if (choice == 1) {
                System.out.println("This project is a paper trading stock exchange platform that allows users to practice trading stocks and ETFs using simulated money. The system uses the Twelve Data API to pull real-time stock data so users can research investments and manage a simulated portfolio.");
                System.out.println("For simplicity, the term stock is used throughout this document to refer to any investment.");
            } else if (choice == 2) {
                System.out.println("Enter in a stock (including publicly-traded mutual funds or exchange-traded funds) symbol to display:");
                System.out.println("\t- The stock ticker and company name");
                System.out.println("\t- The current date & time of the quote pulled");
                System.out.println("\t- The opening price");
                System.out.println("\t- The high of the day");
                System.out.println("\t- The low of the day");
                System.out.println("\t- The previous day's closing price");
                System.out.println("\t- The volume (how many stocks of that type have been traded)");
                System.out.println("\t- The change (in dollars and by percent)");
                System.out.println("\t- A 52-week summary (low, high, change, range, all across the past 52 weeks)");
                System.out.println("The user can then refresh the live price, find similar stocks, or get a time series.");
                System.out.println("A time series shows a series of opening, high, and low prices across a distinct range of time.");
            } else if (choice == 3) {
                System.out.println("Upon doing research, stocks can be purchased through shares or dollars.");
                System.out.println("The live price will be updated before the transaction is completed.");
                System.out.println("The transaction must not exceed the current cash balance.");
            } else if (choice == 4) {
                System.out.println("When purchasing a stock, the user may choose to buy a historical / what-if position.");
                System.out.println("The user selects the date they want the stock to be priced at, then can purchase shares at that price.");
                System.out.println("These purchases will not impact total portfolio value.");
                System.out.println("These purchases cannot be sold for actual cash.");
                System.out.println("They must be removed from the portfolio rather than sold.");
            } else if (choice == 5) {
                System.out.println("The portfolio displays all the investments (real and what-if) a user has made, and contains:");
                System.out.println("\t- The date the investment was made");
                System.out.println("\t- How much the investment has changed in value, by dollars and by percent");
                System.out.println("\t- A live price stream of each distinct stock held");
            } else if (choice == 6) {
                System.out.println("Investments that are not historical / what-if purchases can either be sold or removed.");
                System.out.println("Historical / what-if investments can only be removed.");
                System.out.println("Selling an investment will increase the cash balance by the investment's value.");
                System.out.println("Removing an investment will not increase the cash balance.");
            } else if (choice == 7) {
                System.out.println("Each investment, even if made on the same day, is denoted separately.");
                System.out.println("This ensures that the proper investment is sold or removed when selling or removing investments.");
                System.out.println("Every investment purchase, excluding historical / what-if transactions, is included in a transaction log.");
                System.out.println("This is because historical / what-if investments do not impact the portfolio's actual value.");
            } else if (choice == 8) {
                System.out.println("The definitions menu outlines basic investment and data terms such as quote and time series.");
                System.out.println("The assistance menu guides users on how to utilize the platform.");
            } else if (choice == 9) {
                System.out.println("Each user account has:");
                System.out.println("\t- A distinct username");
                System.out.println("\t- A password");
                System.out.println("\t- A portfolio and corresponding transaction log");
                System.out.println("Each user starts with $10,000.");
                System.out.println("Users may only gain or lose money beyond the initial $10,000 through realized gains or losses from selling stocks.");
                System.out.println("Since the money is simulated, there is no limit to how much a user can gain or lose.");
            } else if (choice == 10) {
                System.out.println("Individual users can create multiple accounts if they choose to start over.");
                System.out.println("All transaction and investment data is stored in a user's distinct json file.");
            } else if (choice == 11) {
                System.out.println("API Limitations:");
                System.out.println("\t- Only 800 API calls can be made per day");
                System.out.println("\t- Only 8 API calls can be made per minute");
                System.out.println("\t- All searches must be performed using the stock symbol");
                System.out.println("\t- The platform cannot search based on company or fund name");
                System.out.println("Platform Limitations:");
                System.out.println("\t- Users cannot take on debt to purchase stocks");
                System.out.println("\t- They may only spend the cash they currently have");
                System.out.println("\t- Users must buy and sell at market value only");
                System.out.println("\t- No stop-loss or stop-limit orders are supported");
                System.out.println("\t- Only publicly available companies and ETFs / mutual funds may be traded");
                System.out.println("\t- Commodities and currencies that are not tracked through an ETF / mutual fund are excluded");
            } else if (choice == 12) {
                running = false;
            } else {
                System.out.println("Invalid input. Please enter an integer 1-12.");
            }

            System.out.println();
        }
    }
}