package api;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Scanner;

public class fetchData {
    public static void main(String[] args) throws Exception {
        String key = apiKey.getApiKey();
        String symbol = "AAPL";

        // this only calls price for now
        String website = "https://api.twelvedata.com/price?symbol=" + symbol + "&apikey=" + key;
        
        URI uri = new URI(website);
        URL url = uri.toURL();

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");

        Scanner input = new Scanner(urlConnection.getInputStream());

        while (input.hasNext()) {
            System.out.println(input.nextLine());
        }

        input.close();
        urlConnection.disconnect();
    }
}