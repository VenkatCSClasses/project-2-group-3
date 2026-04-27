package api;

import com.google.gson.*;
import stock.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetYahooChart {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final ZoneId NY = ZoneId.of("America/New_York");

    public static TimeSeries run(String symbol) throws Exception {
        String urlStr = "https://query1.finance.yahoo.com/v8/finance/chart/"
                + symbol + "?interval=1d&range=1y";

        HttpURLConnection conn = (HttpURLConnection) new URI(urlStr).toURL().openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)");
        conn.setRequestProperty("Accept", "application/json");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);

        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } finally {
            conn.disconnect();
        }

        JsonObject root = JsonParser.parseString(response.toString()).getAsJsonObject();
        JsonObject chartResult = root.getAsJsonObject("chart")
                                     .getAsJsonArray("result")
                                     .get(0).getAsJsonObject();

        JsonObject meta       = chartResult.getAsJsonObject("meta");
        JsonArray  timestamps = chartResult.getAsJsonArray("timestamp");

        JsonObject quoteData = chartResult.getAsJsonObject("indicators")
                                          .getAsJsonArray("quote")
                                          .get(0).getAsJsonObject();

        JsonArray opens   = quoteData.getAsJsonArray("open");
        JsonArray highs   = quoteData.getAsJsonArray("high");
        JsonArray lows    = quoteData.getAsJsonArray("low");
        JsonArray closes  = quoteData.getAsJsonArray("close");
        JsonArray volumes = quoteData.getAsJsonArray("volume");

        List<Values> valuesList = new ArrayList<>();

        for (int i = 0; i < timestamps.size(); i++) {
            if (closes.get(i).isJsonNull()) continue;

            String date = Instant.ofEpochSecond(timestamps.get(i).getAsLong())
                                 .atZone(NY)
                                 .toLocalDate()
                                 .format(FMT);

            double open   = opens.get(i).isJsonNull()   ? 0 : opens.get(i).getAsDouble();
            double high   = highs.get(i).isJsonNull()   ? 0 : highs.get(i).getAsDouble();
            double low    = lows.get(i).isJsonNull()    ? 0 : lows.get(i).getAsDouble();
            double close  = closes.get(i).getAsDouble();
            double volume = volumes.get(i).isJsonNull() ? 0 : volumes.get(i).getAsDouble();

            valuesList.add(new Values(date, open, high, low, close, volume));
        }

        // Yahoo returns oldest-first; reverse to match Twelve Data convention (newest-first)
        Collections.reverse(valuesList);

        Meta m = new Meta(meta.get("symbol").getAsString(), "1day", "USD",
                          "America/New_York", "NYSE", "Common Stock");
        return new TimeSeries(m, valuesList);
    }
}
