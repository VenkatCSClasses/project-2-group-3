package api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Maintains a persistent WebSocket connection to TwelveData and keeps a
 * thread-safe map of the latest price for every subscribed symbol.
 *
 * Usage:
 *   PriceStream stream = new PriceStream();
 *   stream.connect();
 *   stream.subscribe(List.of("AAPL", "MSFT"));
 *   Double price = stream.getPrice("AAPL"); // null until first tick arrives
 *   stream.close();
 */
public class PriceStream {

    private static final String WS_URL = "wss://ws.twelvedata.com/v1/quotes/price";

    private final HttpClient client = HttpClient.newHttpClient();
    private WebSocket ws;
    private final ConcurrentHashMap<String, Double> prices = new ConcurrentHashMap<>();
    private volatile boolean connected = false;
    private final CountDownLatch connectLatch = new CountDownLatch(1);

    /**
     * Connects to the WebSocket server. Blocks up to 5 seconds waiting for the
     * handshake to complete. Safe to call once; do not call again after close().
     */
    public void connect() {
        String apiKey = ApiKey.getApiKey();

        WebSocket.Listener listener = new WebSocket.Listener() {
            private final StringBuilder buffer = new StringBuilder();

            @Override
            public void onOpen(WebSocket ws) {
                connected = true;
                connectLatch.countDown();
                WebSocket.Listener.super.onOpen(ws);
            }

            @Override
            public CompletionStage<?> onText(WebSocket ws, CharSequence data, boolean last) {
                buffer.append(data);
                if (last) {
                    handleMessage(buffer.toString());
                    buffer.setLength(0);
                }
                return WebSocket.Listener.super.onText(ws, data, last);
            }

            @Override
            public CompletionStage<?> onClose(WebSocket ws, int statusCode, String reason) {
                connected = false;
                return WebSocket.Listener.super.onClose(ws, statusCode, reason);
            }

            @Override
            public void onError(WebSocket ws, Throwable error) {
                connected = false;
                connectLatch.countDown();
            }
        };

        try {
            ws = client.newWebSocketBuilder()
                    .buildAsync(URI.create(WS_URL + "?apikey=" + apiKey), listener)
                    .join();
            connectLatch.await(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            connected = false;
        }
    }

    /** Subscribe to one or more ticker symbols. */
    public void subscribe(Collection<String> tickers) {
        if (!connected || tickers.isEmpty()) return;
        JsonObject params = new JsonObject();
        params.addProperty("symbols", String.join(",", tickers));
        JsonObject msg = new JsonObject();
        msg.addProperty("action", "subscribe");
        msg.add("params", params);
        ws.sendText(msg.toString(), true);
    }

    /** Convenience overload for a single ticker. */
    public void subscribe(String ticker) {
        subscribe(List.of(ticker));
    }

    /**
     * Returns the most recently received price for the symbol,
     * or null if no tick has arrived yet.
     */
    public Double getPrice(String ticker) {
        return prices.get(ticker.toUpperCase());
    }

    public boolean isConnected() {
        return connected;
    }

    /** Gracefully closes the WebSocket connection. */
    public void close() {
        if (ws != null && connected) {
            try {
                ws.sendClose(WebSocket.NORMAL_CLOSURE, "done").join();
            } catch (Exception ignored) {}
        }
        client.close();
        connected = false;
    }

    private void handleMessage(String raw) {
        try {
            JsonObject msg = JsonParser.parseString(raw).getAsJsonObject();
            if (msg.has("event") && "price".equals(msg.get("event").getAsString())) {
                String symbol = msg.get("symbol").getAsString().toUpperCase();
                double price = msg.get("price").getAsDouble();
                prices.put(symbol, price);
            }
        } catch (Exception ignored) {}
    }
}
