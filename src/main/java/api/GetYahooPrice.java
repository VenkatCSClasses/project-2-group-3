package api;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;

public class GetYahooPrice {

    public static double run(String symbol) throws Exception {
        String urlStr = "https://query1.finance.yahoo.com/v8/finance/chart/"
                + symbol.toUpperCase() + "?interval=1d&range=5d";

        HttpURLConnection conn = (HttpURLConnection) new URI(urlStr).toURL().openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)");
        conn.setRequestProperty("Accept", "application/json");
        conn.setConnectTimeout(8000);
        conn.setReadTimeout(8000);

        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) response.append(line);
        } finally {
            conn.disconnect();
        }

        JsonObject root = JsonParser.parseString(response.toString()).getAsJsonObject();
        return root.getAsJsonObject("chart")
                   .getAsJsonArray("result")
                   .get(0).getAsJsonObject()
                   .getAsJsonObject("meta")
                   .get("regularMarketPrice").getAsDouble();
    }
}
