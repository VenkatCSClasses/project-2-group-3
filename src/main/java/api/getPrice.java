package api;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Scanner;

import com.google.gson.*;
import stock.*;

public class GetPrice {
    public static Price run() throws Exception {
        String key = ApiKey.getApiKey();
        String symbol = Symbol.getSymbol();

        String website = "https://api.twelvedata.com/price?symbol=" + symbol + "&apikey=" + key;

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
        String price = root.get("price").getAsString();

        return new Price(symbol, price);
    }

    public static void main(String[] args) throws Exception {
        Price price = run();
        System.out.println(price);
    }
}