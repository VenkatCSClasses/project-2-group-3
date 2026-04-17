package api;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.*;

import com.google.gson.*;
import stock.*;

public class SymbolSearch {

    public static List<SymbolSearchResult> run(String symbol) throws Exception {
        String key = ApiKey.getApiKey();

        String website = "https://api.twelvedata.com/symbol_search?symbol=" + symbol + "&apikey=" + key;

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
        JsonArray dataArray = root.getAsJsonArray("data");

        List<SymbolSearchResult> results = new ArrayList<>();

        for (int i = 0; i < dataArray.size(); i++) {
            JsonObject item = dataArray.get(i).getAsJsonObject();

            results.add(new SymbolSearchResult(
                item.get("symbol").getAsString(), 
                item.get("instrument_name").getAsString(),
                item.get("exchange").getAsString(),
                item.get("exchange_timezone").getAsString(),
                item.get("instrument_type").getAsString(), 
                item.get("country").getAsString(), 
                item.get("currency").getAsString()
            ));
        }

        return results;
    }

}