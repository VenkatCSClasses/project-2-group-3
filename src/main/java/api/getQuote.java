package api;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Scanner;

import com.google.gson.*;
import stock.*;

public class GetQuote {
    public static Quote run() throws Exception {
        String key = ApiKey.getApiKey();
        String symbol = Symbol.getSymbol();

        String website = "https://api.twelvedata.com/quote?symbol=" + symbol + "&apikey=" + key;

        URI uri = new URI(website);
        URL url = uri.toURL();

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");

        Scanner input = new Scanner(urlConnection.getInputStream());

        StringBuilder response = new StringBuilder();
        while (input.hasNext()) {
            response.append(input.nextLine());
        }

        input.close();
        urlConnection.disconnect();

        JsonObject root = JsonParser.parseString(response.toString()).getAsJsonObject();

        String name = root.get("name").getAsString();
        String exchange = root.get("exchange").getAsString();
        String currency = root.get("currency").getAsString();
        String datetime = root.get("datetime").getAsString();
        String last_quote_at = root.get("last_quote_at").getAsString();
        String open = root.get("open").getAsString();
        String high = root.get("high").getAsString();
        String low = root.get("low").getAsString();
        String close = root.get("close").getAsString();
        String volume = root.get("volume").getAsString();
        String previous_close = root.get("previous_close").getAsString();
        String change = root.get("change").getAsString();
        String percent_change = root.get("percent_change").getAsString();
        //String fifty_two_week = root.get("fifty_two_week").getAsString(); // Need to make separate class

        return new Quote(symbol, name, exchange, currency, datetime, last_quote_at, open, high, low, close, volume,
            previous_close, change, percent_change);
    }

    public static void main(String[] args) throws Exception {
        Quote quote = run();
        System.out.println(quote);
    }
}