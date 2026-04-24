package apistream;

import api.GetPrice;

/**
* Returns the best available price for a symbol.
* Preference: WebSocket stream → HTTP fallback.
* Falls back to 0.0 only if both fail (caller should handle that case).
*/

public class ResolvePrice {

        public static double resolvePrice(String symbol, PriceStream stream) {
        Double streamPrice = stream.getPrice(symbol);
        if (streamPrice != null) return streamPrice;

        // HTTP fallback
        try {
            return GetPrice.run(symbol).getPrice();
        } catch (Exception e) {
            return 0.0;
        }
    }

}
