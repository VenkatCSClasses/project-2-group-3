package misc;

import java.text.NumberFormat;
import java.util.Locale;

public class NumFormat {

    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
    private static final NumberFormat percentFormat = NumberFormat.getPercentInstance();
    private static final NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);

    static {
        currencyFormat.setMinimumFractionDigits(2);
        currencyFormat.setMaximumFractionDigits(2);

        percentFormat.setMinimumFractionDigits(2);
        percentFormat.setMaximumFractionDigits(2);

        numberFormat.setMinimumFractionDigits(0);
        numberFormat.setMaximumFractionDigits(2);
    }

    public static String formatCurrency(double value) {
        return currencyFormat.format(value);
    }

    public static String formatPercent(double value) {
        return percentFormat.format(value);
    }

    public static String formatNumber(double value) {
        return numberFormat.format(value);
    }
}