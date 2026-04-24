package trade;

import api.GetPrice;
import api.PriceStream;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.function.Consumer;

@RestController
@RequestMapping("/prices")
public class PriceController {

    private final PriceStream priceStream;

    @Autowired
    public PriceController(PriceStream priceStream) {
        this.priceStream = priceStream;
    }

    @GetMapping("/{ticker}")
    public ResponseEntity<?> getPrice(@PathVariable String ticker, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return ResponseEntity.status(401).body("Please log in first.");
        }
        try {
            double price = GetPrice.run(ticker.toUpperCase()).getPrice();
            return ResponseEntity.ok(Map.of("ticker", ticker.toUpperCase(), "price", price));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Could not fetch price for " + ticker);
        }
    }

    @GetMapping(value = "/{ticker}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamPrice(@PathVariable String ticker, HttpSession session) {
        SseEmitter emitter = new SseEmitter(0L);

        if (session.getAttribute("username") == null) {
            emitter.complete();
            return emitter;
        }

        String symbol = ticker.toUpperCase();
        priceStream.subscribe(symbol);

        Consumer<Double> listener = price -> {
            try {
                emitter.send(SseEmitter.event().data(Map.of("ticker", symbol, "price", price)));
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        };

        priceStream.addListener(symbol, listener);
        emitter.onCompletion(() -> priceStream.removeListener(symbol, listener));
        emitter.onTimeout(() -> priceStream.removeListener(symbol, listener));
        emitter.onError(e -> priceStream.removeListener(symbol, listener));

        return emitter;
    }
}
