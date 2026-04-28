package trade;

import api.*;
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

    private String apiLimitMessage(String ticker) {
        return "Price data is currently unavailable for " + ticker.toUpperCase()
        + ". The stock API may be out of daily credits, rate-limited, or unable to return this ticker right now.";
    }

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
            return ResponseEntity.badRequest().body(apiLimitMessage(req.ticker()));
        }

        Investment inv = UserTrading.purchaseStock(user, req.ticker(), req.companyName(), price, req.method(), req.amount());
        if (inv == null) return ResponseEntity.badRequest().body("Purchase failed — insufficient funds or invalid amount.");

        double cashDeducted = inv.getShares() * inv.getPurchasePrice();
        userService.saveUser(user);

        Map<String, Object> response = buildPortfolioResponse(user);
        response.put("cashDeducted", cashDeducted);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/sell")
    public ResponseEntity<?> sell(@RequestBody SellRequest req, HttpSession session) {
        User user = sessionUser(session);
        if (user == null) return ResponseEntity.status(401).body("Please log in first.");

        double price;
        try {
            price = GetYahooPrice.run(req.ticker());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(apiLimitMessage(req.ticker()));
        }

        Investment found = UserTrading.findInvestmentById(user, req.investmentId());
        if (found == null) return ResponseEntity.badRequest().body("Investment not found.");

        boolean ok = UserTrading.sellStock(
                user,
                found.getInvestmentId(),
                found.getTicker(),
                price,
                req.method(),
                req.amount()
        );

        if (!ok) return ResponseEntity.badRequest().body("Sale failed — check share count or dollar amount.");

        userService.saveUser(user);
        return ResponseEntity.ok(buildPortfolioResponse(user));
    }

    @DeleteMapping("/{investmentId}")
    public ResponseEntity<?> remove(@PathVariable int investmentId, HttpSession session) {
        User user = sessionUser(session);
        if (user == null) return ResponseEntity.status(401).body("Please log in first.");

        Investment inv = UserTrading.findInvestmentById(user, investmentId);
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
    record SellRequest(int investmentId, String ticker, String method, double amount) {}
}
