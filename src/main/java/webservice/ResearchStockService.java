package webservice;

import api.ApiKey;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ResearchStockService {

    private static final String BASE_URL = "https://api.twelvedata.com";
    private static final long CACHE_TTL_MS = 5 * 60 * 1000;
    private static boolean USE_TIME_SERIES = true;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    private static class CacheEntry {
        final ResearchStock stock;
        final long timestamp;
        CacheEntry(ResearchStock stock) { this.stock = stock; this.timestamp = System.currentTimeMillis(); }
        boolean isExpired() { return System.currentTimeMillis() - timestamp > CACHE_TTL_MS; }
    }

    public ResearchStock getStockResearch(String ticker) {
        String cacheKey = ticker.toUpperCase();
        CacheEntry cached = cache.get(cacheKey);
        if (cached != null && !cached.isExpired()) return cached.stock;
        try {
            String apiKey = ApiKey.getApiKey();

            String quoteUrl = BASE_URL + "/quote?symbol=" + ticker + "&apikey=" + apiKey;
            String quoteJson = restTemplate.getForObject(quoteUrl, String.class);
            JsonNode quote = objectMapper.readTree(quoteJson);

            if ("error".equals(quote.path("status").asText())) {
                String message = quote.path("message").asText("").toLowerCase();

                if (message.contains("symbol") ||
                    message.contains("not found") ||
                    message.contains("invalid") ||
                    message.contains("does not exist")) {
                    throw new IllegalArgumentException(
                        "Invalid stock symbol. Please enter a valid ticker such as AAPL, MSFT, or NVDA."
                    );
                }

                throw new RuntimeException(
                    "Stock data is currently unavailable. The API may be rate-limited or temporarily unavailable."
                );
            }

            JsonNode timeSeries = null;
            
            if (USE_TIME_SERIES) {
                try {
                    String tsUrl = BASE_URL + "/time_series?symbol=" + ticker + "&interval=1day&outputsize=260&apikey=" + apiKey;
                    String tsJson = restTemplate.getForObject(tsUrl, String.class);
                    timeSeries = objectMapper.readTree(tsJson);
                } catch (Exception ignored) {
                    timeSeries = null;
                }
            }

            if ("error".equals(quote.path("status").asText())) {
                String message = quote.path("message").asText("").toLowerCase();

                if (message.contains("symbol") || message.contains("not found")) {
                    throw new IllegalArgumentException("Invalid stock symbol. Please enter a valid ticker such as AAPL, MSFT, or NVDA.");
                }

                throw new RuntimeException("Stock quote data is currently unavailable. The API may be rate-limited or temporarily unavailable.");
            }

            if (timeSeries != null && "error".equals(timeSeries.path("status").asText())) {
                timeSeries = null;
            }

            ResearchStock stock = new ResearchStock();
            stock.setTicker(ticker.toUpperCase());

            if (quote.path("name").isMissingNode() ||
                quote.path("close").isMissingNode() ||
                quote.path("open").isMissingNode() ||
                quote.path("volume").isMissingNode()) {
                throw new IllegalArgumentException(
                    "Invalid stock symbol. Please enter a valid ticker such as AAPL, MSFT, or NVDA."
                );
            }

            stock.setCompanyName(quote.get("name").asText());

            double currentClose = Double.parseDouble(quote.get("close").asText());
            stock.setLastClosingPrice(currentClose);
            stock.setLastOpeningPrice(Double.parseDouble(quote.get("open").asText()));
            stock.setVolume(Long.parseLong(quote.get("volume").asText()));

            stock.setOneDayPriceChange(Double.parseDouble(quote.get("change").asText()));
            stock.setOneDayPercentChange(Double.parseDouble(quote.get("percent_change").asText()));

            JsonNode values = timeSeries == null ? null : timeSeries.get("values");
            if (values != null && values.isArray()) {
                List<Double> closes = new ArrayList<>();
                List<String> dates = new ArrayList<>();

                for (JsonNode v : values) {
                    closes.add(Double.parseDouble(v.get("close").asText()));
                    dates.add(v.get("datetime").asText());
                }

                applyChange(stock, currentClose, closes, 5, "week");
                applyChange(stock, currentClose, closes, 22, "month");
                applyChange(stock, currentClose, closes, 66, "threeMonth");
                applyChange(stock, currentClose, closes, 132, "sixMonth");
                applyYTD(stock, currentClose, closes, dates);
            }

            cache.put(cacheKey, new CacheEntry(stock));
            return stock;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Market data is currently unavailable for " + ticker + 
            ". The stock API may be out of daily credits or rate-limited. Try again later.", e);
         }

    }

    private void applyChange(ResearchStock stock, double current, List<Double> closes, int daysBack, String period) {
        if (closes.size() <= daysBack) return;
        double past = closes.get(daysBack);
        double change = round(current - past);
        double pct = round((change / past) * 100);
        switch (period) {
            case "week"       -> { stock.setOneWeekPriceChange(change);   stock.setOneWeekPercentChange(pct); }
            case "month"      -> { stock.setOneMonthPriceChange(change);  stock.setOneMonthPercentChange(pct); }
            case "threeMonth" -> { stock.setThreeMonthPriceChange(change); stock.setThreeMonthPercentChange(pct); }
            case "sixMonth"   -> { stock.setSixMonthPriceChange(change);  stock.setSixMonthPercentChange(pct); }
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
        double pct = round((change / base) * 100);
        stock.setYearToDatePriceChange(change);
        stock.setYearToDatePercentChange(pct);
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
