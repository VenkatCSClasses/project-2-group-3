package api;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Scanner;

import com.google.gson.*;
import stock.*;

public class GetQuote {
    public static Quote run(String symbol) throws Exception {
        String key = ApiKey.getApiKey();

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
        double open = root.get("open").getAsDouble();
        double high = root.get("high").getAsDouble();
        double low = root.get("low").getAsDouble();
        double close = root.get("close").getAsDouble();
        double volume = root.get("volume").getAsDouble();
        double previous_close = root.get("previous_close").getAsDouble();
        double change = root.get("change").getAsDouble();
        double percent_change = root.get("percent_change").getAsDouble();

        JsonObject fiftyTwoWeekObject = root.getAsJsonObject("fifty_two_week");

        FiftyTwoWeek fiftyTwoWeek = new FiftyTwoWeek(
            fiftyTwoWeekObject.get("low").getAsDouble(),
            fiftyTwoWeekObject.get("high").getAsDouble(),
            fiftyTwoWeekObject.get("low_change").getAsDouble(),
            fiftyTwoWeekObject.get("high_change").getAsDouble(),
            fiftyTwoWeekObject.get("low_change_percent").getAsDouble(),
            fiftyTwoWeekObject.get("high_change_percent").getAsDouble(),
            fiftyTwoWeekObject.get("range").getAsString()
        );

        return new Quote(symbol, name, exchange, currency, datetime, last_quote_at, open, high, low, close, volume,
            previous_close, change, percent_change, fiftyTwoWeek);
    }
}