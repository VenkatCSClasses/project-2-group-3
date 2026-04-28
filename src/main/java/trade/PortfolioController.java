package trade;

import api.GetYahooPrice;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webservice.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getPortfolio(HttpSession session) {
        User user = sessionUser(session);
        if (user == null) return ResponseEntity.status(401).body("Please log in first.");

        for (Investment inv : user.getPortfolio().getInvestments()) {
            try {
                inv.setCurrentPrice(GetYahooPrice.run(inv.getTicker()));
            } catch (Exception ignored) {}
        }

        return ResponseEntity.ok(buildPortfolioResponse(user));
    }

    @PostMapping("/buy")
    public ResponseEntity<?> buy(@RequestBody BuyRequest req, HttpSession session) {
        User user = sessionUser(session);
        if (user == null) return ResponseEntity.status(401).body("Please log in first.");

        double price;
        try {
            price = GetYahooPrice.run(req.ticker());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Could not fetch price for " + req.ticker());
        }

        Investment inv = UserTrading.purchaseStock(user, req.ticker(), req.companyName(), price, req.method(), req.amount());
        if (inv == null) return ResponseEntity.badRequest().body("Purchase failed — insufficient funds or invalid amount.");

        userService.saveUser(user);
        return ResponseEntity.ok(buildPortfolioResponse(user));
    }

    @PostMapping("/sell")
    public ResponseEntity<?> sell(@RequestBody SellRequest req, HttpSession session) {
        User user = sessionUser(session);
        if (user == null) return ResponseEntity.status(401).body("Please log in first.");

        double price;
        try {
            price = GetYahooPrice.run(req.ticker());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Could not fetch price for " + req.ticker());
        }

        // Collect ALL positions for this ticker (handles duplicate entries from past buys)
        List<Investment> positions = new ArrayList<>();
        for (Investment inv : user.getPortfolio().getInvestments()) {
            if (inv.getTicker().equalsIgnoreCase(req.ticker()) && inv.getInvestmentType() == 0)
                positions.add(inv);
        }
        if (positions.isEmpty()) return ResponseEntity.badRequest().body("Investment not found.");

        double totalShares = positions.stream().mapToDouble(Investment::getShares).sum();

        double sharesToSell;
        if (req.method().equalsIgnoreCase("shares")) {
            sharesToSell = req.amount();
        } else if (req.method().equalsIgnoreCase("dollars")) {
            sharesToSell = req.amount() / price;
        } else {
            return ResponseEntity.badRequest().body("Invalid sell method.");
        }

        if (sharesToSell <= 0 || sharesToSell > totalShares + 0.0001)
            return ResponseEntity.badRequest().body("Sale failed — not enough shares.");

        // Deduct shares across positions in order, removing any that hit zero
        double remaining = sharesToSell;
        for (Investment pos : positions) {
            if (remaining <= 0.0001) break;
            double take = Math.min(pos.getShares(), remaining);
            pos.removeShares(take);
            remaining -= take;
            if (pos.getShares() < 0.0001) user.getPortfolio().removeInvestment(pos);
        }

        double proceeds = sharesToSell * price;
        user.setCashBalance(user.getCashBalance() + proceeds);
        user.getTransactionLog().addTransaction(new Transaction("Sell", req.ticker(), sharesToSell, price, proceeds));

        userService.saveUser(user);
        return ResponseEntity.ok(buildPortfolioResponse(user));
    }

    @DeleteMapping("/{ticker}")
    public ResponseEntity<?> remove(@PathVariable String ticker, HttpSession session) {
        User user = sessionUser(session);
        if (user == null) return ResponseEntity.status(401).body("Please log in first.");

        Investment inv = UserTrading.findInvestment(user, ticker);
        if (inv == null) return ResponseEntity.badRequest().body("Investment not found.");

        user.getPortfolio().removeInvestment(inv);
        userService.saveUser(user);
        return ResponseEntity.ok(buildPortfolioResponse(user));
    }

    private User sessionUser(HttpSession session) {
        String username = (String) session.getAttribute("username");
        return username == null ? null : userService.getUser(username);
    }

    private Map<String, Object> buildPortfolioResponse(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", user.getUsername());
        map.put("cashBalance", user.getCashBalance());
        map.put("investments", user.getPortfolio().getInvestments());
        map.put("totalValue", user.getPortfolio().getRealTotalValue());
        map.put("totalChange", user.getPortfolio().getRealTotalChange());
        return map;
    }

    record BuyRequest(String ticker, String companyName, String method, double amount) {}
    record SellRequest(String ticker, String method, double amount) {}
}
