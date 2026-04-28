package webservice;

import com.google.gson.*;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ResearchStockService {

    private static final long CACHE_TTL_MS = 5 * 60 * 1000;
    private static final ZoneId NY = ZoneId.of("America/New_York");
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    private static class CacheEntry {
        final ResearchStock stock;
        final long timestamp;

        CacheEntry(ResearchStock stock) {
            this.stock = stock;
            this.timestamp = System.currentTimeMillis();
        }

        boolean isExpired() {
            return System.currentTimeMillis() - timestamp > CACHE_TTL_MS;
        }
    }

    public ResearchStock getStockResearch(String ticker) {
        String cacheKey = ticker.toUpperCase();
        CacheEntry cached = cache.get(cacheKey);

        if (cached != null && !cached.isExpired()) {
            return cached.stock;
        }

        try {
            String symbol = ticker.toUpperCase();

            JsonObject root = fetchYahoo(
                "https://query1.finance.yahoo.com/v8/finance/chart/"
                    + symbol + "?interval=1d&range=1y"
            );

            JsonObject chart = root.getAsJsonObject("chart");

            if (chart == null || chart.get("result") == null || chart.get("result").isJsonNull()) {
                throw new IllegalArgumentException(
                    "Invalid stock symbol. Please enter a valid ticker such as AAPL, MSFT, or NVDA."
                );
            }

            JsonArray results = chart.getAsJsonArray("result");

            if (results == null || results.size() == 0) {
                throw new IllegalArgumentException(
                    "Invalid stock symbol. Please enter a valid ticker such as AAPL, MSFT, or NVDA."
                );
            }

            JsonObject result = results.get(0).getAsJsonObject();
            JsonObject meta = result.getAsJsonObject("meta");

            if (meta == null || !meta.has("regularMarketPrice")) {
                throw new IllegalArgumentException(
                    "Invalid stock symbol. Please enter a valid ticker such as AAPL, MSFT, or NVDA."
                );
            }

            double currentPrice = meta.get("regularMarketPrice").getAsDouble();

            double previousClose = meta.has("chartPreviousClose")
                ? meta.get("chartPreviousClose").getAsDouble()
                : currentPrice;

            double openPrice = meta.has("regularMarketOpen")
                ? meta.get("regularMarketOpen").getAsDouble()
                : previousClose;

            long volume = meta.has("regularMarketVolume")
                ? meta.get("regularMarketVolume").getAsLong()
                : 0L;

            String companyName = meta.has("longName")
                ? meta.get("longName").getAsString()
                : meta.has("shortName")
                    ? meta.get("shortName").getAsString()
                    : symbol;

            double dayChange = round(currentPrice - previousClose);
            double dayChangePct = previousClose == 0
                ? 0
                : round((dayChange / previousClose) * 100);

            JsonArray timestamps = result.getAsJsonArray("timestamp");
            JsonArray closes = result.getAsJsonObject("indicators")
                .getAsJsonArray("quote")
                .get(0)
                .getAsJsonObject()
                .getAsJsonArray("close");

            List<Double> closeList = new ArrayList<>();
            List<String> dateList = new ArrayList<>();

            for (int i = 0; i < timestamps.size(); i++) {
                if (closes.get(i).isJsonNull()) {
                    continue;
                }

                closeList.add(closes.get(i).getAsDouble());
                dateList.add(
                    Instant.ofEpochSecond(timestamps.get(i).getAsLong())
                        .atZone(NY)
                        .toLocalDate()
                        .format(FMT)
                );
            }

            List<Double> closesDesc = new ArrayList<>();
            List<String> datesDesc = new ArrayList<>();

            for (int i = closeList.size() - 1; i >= 0; i--) {
                closesDesc.add(closeList.get(i));
                datesDesc.add(dateList.get(i));
            }

            ResearchStock stock = new ResearchStock();
            stock.setTicker(symbol);
            stock.setCompanyName(companyName);
            stock.setLastClosingPrice(currentPrice);
            stock.setLastOpeningPrice(openPrice);
            stock.setVolume(volume);
            stock.setOneDayPriceChange(dayChange);
            stock.setOneDayPercentChange(dayChangePct);

            applyChange(stock, currentPrice, closesDesc, 5, "week");
            applyChange(stock, currentPrice, closesDesc, 22, "month");
            applyChange(stock, currentPrice, closesDesc, 66, "threeMonth");
            applyChange(stock, currentPrice, closesDesc, 132, "sixMonth");

            if (!closesDesc.isEmpty() && !datesDesc.isEmpty()) {
                applyYTD(stock, currentPrice, closesDesc, datesDesc);
            }

            cache.put(cacheKey, new CacheEntry(stock));
            return stock;

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(
                "Market data is currently unavailable for " + ticker
                    + ". The stock API may be out of daily credits or rate-limited. Try again later.",
                e
            );
        }
    }

    private JsonObject fetchYahoo(String urlStr) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URI(urlStr).toURL().openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)");
        conn.setRequestProperty("Accept", "application/json");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);

        StringBuilder sb = new StringBuilder();

        try (BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = r.readLine()) != null) {
                sb.append(line);
            }
        } finally {
            conn.disconnect();
        }

        return JsonParser.parseString(sb.toString()).getAsJsonObject();
    }

    private void applyChange(ResearchStock stock, double current, List<Double> closes, int daysBack, String period) {
        if (closes.size() <= daysBack) {
            return;
        }

        double past = closes.get(daysBack);
        double change = round(current - past);
        double pct = past == 0 ? 0 : round((change / past) * 100);

        switch (period) {
            case "week" -> {
                stock.setOneWeekPriceChange(change);
                stock.setOneWeekPercentChange(pct);
            }
            case "month" -> {
                stock.setOneMonthPriceChange(change);
                stock.setOneMonthPercentChange(pct);
            }
            case "threeMonth" -> {
                stock.setThreeMonthPriceChange(change);
                stock.setThreeMonthPercentChange(pct);
            }
            case "sixMonth" -> {
                stock.setSixMonthPriceChange(change);
                stock.setSixMonthPercentChange(pct);
            }
        }
    }

    private void applyYTD(ResearchStock stock, double current, List<Double> closes, List<String> dates) {
        String yearStart = LocalDate.now().getYear() + "-01-01";
        double base = closes.get(closes.size() - 1); // fallback: oldest available

        for (int i = dates.size() - 1; i >= 0; i--) {
            if (dates.get(i).compareTo(yearStart) < 0) {
                base = closes.get(i);
                break;
            }
        }

        double change = round(current - base);
        double pct = base == 0 ? 0 : round((change / base) * 100);

        stock.setYearToDatePriceChange(change);
        stock.setYearToDatePercentChange(pct);
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}