package api;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Scanner;

import com.google.gson.*;
import stock.*;

public class GetEODPrice {
    public static EODPrice run() throws Exception {
        String key = ApiKey.getApiKey();
        String symbol = Symbol.getSymbol();

        String website = "https://api.twelvedata.com/eod?symbol=" + symbol + "&apikey=" + key;

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

        String exchange = root.get("exchange").getAsString();
        String currency = root.get("currency").getAsString();
        String datetime = root.get("datetime").getAsString();
        String close = root.get("close").getAsString();

        return new EODPrice(symbol, exchange, currency, datetime, close);
    }

    public static void main(String[] args) throws Exception {
        EODPrice price = run();
        System.out.println(price);
    }
}