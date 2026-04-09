package api;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Scanner;

public class fetchData {
    public static void main(String[] args) throws Exception {
        String key = apiKey.getApiKey();
        String symbol = "AAPL";
        String interval = "1min"; // 1min, 5min, 15min, 30min, 45min, 1h, 2h, 4h, 5h, 1day, 1week, 1month

        // maybe just split these into all separate functions/files? >_<

        // will also need tests

        // get current price
        // String website = "https://api.twelvedata.com/price?symbol=" + symbol + "&apikey=" + key;

        // get time series (rice, currency, open, high, low, change, percent change)
        //need to print nicely
        //String website = "https://api.twelvedata.com/time_series?symbol=" + symbol 
        //+ "&interval=" + interval + "&apikey=" + key;

        // get quote
        // String website = "https://api.twelvedata.com/quote?symbol=" + symbol + "&apikey=" + key;

        // get eod price
        // String website = "https://api.twelvedata.com/eod?symbol=" + symbol + "&apikey=" + key;

        // symbol search
        String website = "https://api.twelvedata.com/symbol_search?symbol=" + symbol + "&apikey=" + key;

        // possibly add market research + exchange schedule and asset catalog functionality?

        // note: limitations are largely through api, 800 credits a day and we are using free version

        URI uri = new URI(website);
        URL url = uri.toURL();

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");

        Scanner input = new Scanner(urlConnection.getInputStream());

        while (input.hasNext()) { // fix so this prints out nicely
            System.out.println(input.nextLine());
        }

        input.close();
        urlConnection.disconnect();
    }
}