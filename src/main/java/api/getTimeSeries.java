package api;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.*;

import com.google.gson.*;
import stock.*;

public class GetTimeSeries {
    public static TimeSeries run() throws Exception {
        String key = ApiKey.getApiKey();
        String symbol = Symbol.getSymbol();
        String interval = Interval.getInterval();

        String website = "https://api.twelvedata.com/time_series?symbol=" + symbol 
        + "&interval=" + interval + "&apikey=" + key;

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
        
        JsonObject metaObject = root.getAsJsonObject("meta");

        Meta meta = new Meta(
            metaObject.get("symbol").getAsString(),
            metaObject.get("interval").getAsString(),
            metaObject.get("currency").getAsString(),
            metaObject.get("exchange_timezone").getAsString(),
            metaObject.get("exchange").getAsString(),
            metaObject.get("type").getAsString()
        );

        JsonArray valuesArray = root.getAsJsonArray("values");
        List<Values> valuesList = new ArrayList<>();

        for (int i = 0; i < valuesArray.size(); i++) {
            JsonObject valuesObject = valuesArray.get(i).getAsJsonObject();

            Values values = new Values(
                valuesObject.get("datetime").getAsString(),
                valuesObject.get("open").getAsString(),
                valuesObject.get("high").getAsString(),
                valuesObject.get("low").getAsString(),
                valuesObject.get("close").getAsString(),
                valuesObject.get("volume").getAsString()
            );

            valuesList.add(values);
        }

        return new TimeSeries(meta, valuesList);

    }

    public static void main(String[] args) throws Exception {
        TimeSeries timeSeries = run();

        System.out.println(timeSeries.getMeta());

        for (int i = 0; i < timeSeries.getValues().size(); i++) {
            System.out.println(timeSeries.getValues().get(i));
        }
    }
}