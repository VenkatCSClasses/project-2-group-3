package api;

import java.util.Scanner;

// This code could potentially go in a different class or part of the program that serves for the user to do research
// It is only here as a placeholder to show the API works, as API calls really can't be tested with stock data

public class fetchData {

    public static void main(String[] args) throws Exception {
        // WIP - Take user input for symbol (in Symbol)
        boolean running = true;
        Scanner input = new Scanner(System.in);

        while (running) {
            System.out.println("What action would you like to perform? Enter an integer 1-7.");
            System.out.println("1. Get Current Price ");
            System.out.println("2. Get EOD Price");
            System.out.println("3. Get Quote");
            System.out.println("4: Get Time Series");
            System.out.println("5: Stock Symbol Search");
            System.out.println("6: Retype Stock Symbol (WIP)");
            System.out.println("7: Exit");

            if (!input.hasNextInt()) {
                System.out.println("Invaild input. Please enter an integer.");
                input.nextLine();
            }

            int choice = input.nextInt();
    
            if (choice == 1) {
                //getPrice.run();
            } else if (choice == 2) {
                //getEODPrice.run();
            } else if (choice == 3) {
                //getQuote.run();
            } else if (choice == 4) {
                //getTimeSeries.run();
            } else if (choice == 5) {
                //symbolSearch.run();
            } else if (choice == 6) {
                System.out.println("WIP");
            } else if (choice == 7) {
                System.out.println("Closing program.");
                running = false;
                input.close();
            }
        }


    }
}