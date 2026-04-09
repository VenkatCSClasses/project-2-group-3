package api;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Scanner;

public class symbolSearch {

    public static void run() throws Exception {
        String key = apiKey.getApiKey();
        String symbol = Symbol.getSymbol();

        String website = "https://api.twelvedata.com/symbol_search?symbol=" + symbol + "&apikey=" + key;

        URI uri = new URI(website);
        URL url = uri.toURL();

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");

        Scanner input = new Scanner(urlConnection.getInputStream());

        // WIP - modify structure to properly parse or print into JSON
        while (input.hasNext()) {
            System.out.println(input.nextLine());
        }

        input.close();
        urlConnection.disconnect();
    }

    public static void main(String[] args) throws Exception {
        run();
    }
}