package webservice;

import api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/stocks")
public class ResearchStockController {

    private final ResearchStockService researchStockService;

    private String apiLimitMessage(String ticker) {
        return "Market data is currently unavailable for " + ticker
        + ". The stock API may be out of daily credits or rate-limited. Try again later.";
    }

    @Autowired
    public ResearchStockController(ResearchStockService researchStockService) {
        this.researchStockService = researchStockService;
    }

    @GetMapping("/{ticker}")
    public ResponseEntity<?> getStock(@PathVariable String ticker, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return ResponseEntity.status(401).body("Please log in first.");
        }
  
        try {
            return ResponseEntity.ok(researchStockService.getStockResearch(ticker.toUpperCase()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(apiLimitMessage(ticker));
        }
    }

    @GetMapping("/{ticker}/chart")
    public ResponseEntity<?> getChartData(@PathVariable String ticker, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return ResponseEntity.status(401).body("Please log in first.");
        }
        try {
            return ResponseEntity.ok(GetYahooChart.run(ticker.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                "Market data is currently unavailable for " + ticker.toUpperCase()
                + ". The stock API may be out of daily credits or rate-limited. Try again later.");
        }
    }
}