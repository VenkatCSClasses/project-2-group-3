package api;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Scanner;

public class getTimeSeries {
    public static void main(String[] args) throws Exception {
        String key = apiKey.getApiKey();
        String symbol = Symbol.getSymbol();
        String interval = "1min"; // 1min, 5min, 15min, 30min, 45min, 1h, 2h, 4h, 5h, 1day, 1week, 1month
        //maybe put into different file?

        String website = "https://api.twelvedata.com/time_series?symbol=" + symbol 
        + "&interval=" + interval + "&apikey=" + key;

        URI uri = new URI(website);
        URL url = uri.toURL();

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");

        Scanner input = new Scanner(urlConnection.getInputStream());

        // WIP - modify structure to properly parse or print into JSON
        while (input.hasNext()) {
            System.out.println(input.nextLine());
        }

        input.close();
        urlConnection.disconnect();
    }
}