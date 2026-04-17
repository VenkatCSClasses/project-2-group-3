package api;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.*;
import stock.*;

public class GetEODPrice {
    public static EODPrice run(String symbol) throws Exception {
        String key = ApiKey.getApiKey();

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
        double close = root.get("close").getAsDouble();

        return new EODPrice(symbol, exchange, currency, datetime, close);
    }
}