package CLI;

import java.util.ArrayList;

import trade.Investment;
import trade.Portfolio;
import trade.User;

public class PrintPortfolioSnapshot {
    /** Prints a formatted portfolio snapshot (no I/O prompts). */
    public static void printPortfolioSnapshot(User user) {
        Portfolio portfolio = user.getPortfolio();
        ArrayList<Investment> investments = portfolio.getInvestments();

        System.out.println();

        if (investments.isEmpty()) {
            System.out.println("No investments in portfolio.");
            System.out.printf("Cash: $%.2f%n%n", user.getCashBalance());
            return;
        }

        boolean hasWhatIf = false;

        for (int i = 0; i < investments.size(); i++) {
            Investment inv = investments.get(i);

            String investmentType = "";
            if (inv.getInvestmentType() == 1) {
                investmentType = " [WHAT-IF]";
                hasWhatIf = true;
            }

            System.out.printf("%d. %-30s (%s)%s%n",
                    i + 1,
                    inv.getCompanyName(),
                    inv.getTicker(),
                    investmentType);

            System.out.printf("   Investment Date: %s%n", inv.getPurchaseDate());
            System.out.printf("   Shares: %10.4f | Avg Cost: $%9.4f | Current: $%9.4f%n",
                    inv.getShares(), inv.getPurchasePrice(), inv.getCurrentPrice());
            System.out.printf("   Value:  $%10.2f | Change:   %+.2f%%%n%n",
                    inv.getValue(), inv.getPercentChange());
        }

        System.out.printf("Portfolio Value (Real): $%.2f | Overall Change (Real): %+.2f%%%n",
                portfolio.getRealTotalValue(),
                portfolio.getRealTotalChange());

        if (hasWhatIf) {
            System.out.printf("Portfolio Value (What-If): $%.2f | Overall Change (What-If): %+.2f%%%n",
                    portfolio.getWhatIfTotalValue(),
                    portfolio.getWhatIfTotalChange());
        }

        System.out.printf("Cash: $%.2f%n%n", user.getCashBalance());
    }
}