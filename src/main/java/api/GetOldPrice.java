package api;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.*;

import com.google.gson.*;

public class GetOldPrice {
    public static double run(String symbol,String purchaseDate) throws Exception {
        String key = ApiKey.getApiKey();

        String website = "https://api.twelvedata.com/time_series?end_date=" + purchaseDate +
        "&outputsize=1&symbol=" + symbol + "&interval=1day&apikey=" + key;

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
        JsonArray valuesArray = root.getAsJsonArray("values");
        JsonObject valuesObject = valuesArray.get(0).getAsJsonObject();

        if (valuesArray == null || valuesArray.size() == 0) {
            throw new Exception("Error: No historical price data found.");
        }

        return valuesObject.get("close").getAsDouble();

    }
}