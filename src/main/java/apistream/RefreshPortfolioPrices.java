package apistream;

import api.*;
import trade.Investment;
import trade.Portfolio;

public class RefreshPortfolioPrices {

    public static void refreshPortfolioPrices(Portfolio portfolio, PriceStream stream) {
        for (Investment inv : portfolio.getInvestments()) {
            Double sp = stream.getPrice(inv.getTicker());
            if (sp != null) {
                inv.setCurrentPrice(sp);
            } else {
                try {
                    inv.setCurrentPrice(GetPrice.run(inv.getTicker()).getPrice());
                } catch (Exception ignored) {}
            }
        }
    }
}
