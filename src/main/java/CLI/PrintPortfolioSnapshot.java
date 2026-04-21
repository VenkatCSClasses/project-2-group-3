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
        for (int i = 0; i < investments.size(); i++) {
            Investment inv = investments.get(i);
            System.out.printf("%d. %-30s (%s)%n", i + 1, inv.getCompanyName(), inv.getTicker());
            System.out.printf("   Shares: %10.4f | Avg Cost: $%9.4f | Current: $%9.4f%n",
                    inv.getShares(), inv.getPurchasePrice(), inv.getCurrentPrice());
            System.out.printf("   Value:  $%10.2f | Change:   %+.2f%%%n%n",
                    inv.getValue(), inv.getPercentChange());
        }

        System.out.printf("Portfolio Value: $%.2f | Overall Change: %+.2f%% | Cash: $%.2f%n%n",
                portfolio.getTotalValue(), portfolio.getTotalChange(), user.getCashBalance());
    }
}