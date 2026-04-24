package trade;

import api.GetPrice;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
                inv.setCurrentPrice(GetPrice.run(inv.getTicker()).getPrice());
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
            price = GetPrice.run(req.ticker()).getPrice();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Could not fetch price for " + req.ticker());
        }

        boolean ok = user.purchaseStock(req.ticker(), req.companyName(), price, req.method(), req.amount());
        if (!ok) return ResponseEntity.badRequest().body("Purchase failed — insufficient funds or invalid amount.");

        userService.saveUser(user);
        return ResponseEntity.ok(buildPortfolioResponse(user));
    }

    @PostMapping("/sell")
    public ResponseEntity<?> sell(@RequestBody SellRequest req, HttpSession session) {
        User user = sessionUser(session);
        if (user == null) return ResponseEntity.status(401).body("Please log in first.");

        double price;
        try {
            price = GetPrice.run(req.ticker()).getPrice();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Could not fetch price for " + req.ticker());
        }

        boolean ok = user.sellStock(req.ticker(), price, req.method(), req.amount());
        if (!ok) return ResponseEntity.badRequest().body("Sale failed — check share count or dollar amount.");

        userService.saveUser(user);
        return ResponseEntity.ok(buildPortfolioResponse(user));
    }

    @DeleteMapping("/{ticker}")
    public ResponseEntity<?> remove(@PathVariable String ticker, HttpSession session) {
        User user = sessionUser(session);
        if (user == null) return ResponseEntity.status(401).body("Please log in first.");

        Investment inv = user.findInvestment(ticker);
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
        map.put("totalValue", user.getPortfolio().getTotalValue());
        map.put("totalChange", user.getPortfolio().getTotalChange());
        return map;
    }

    record BuyRequest(String ticker, String companyName, String method, double amount) {}
    record SellRequest(String ticker, String method, double amount) {}
}
