package trade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/stocks")
public class ResearchStockController {

    private final ResearchStockService researchStockService;

    @Autowired
    public ResearchStockController(ResearchStockService researchStockService) {
        this.researchStockService = researchStockService;
    }

    @GetMapping("/{ticker}")
    public ResponseEntity<?> getStock(@PathVariable String ticker, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return ResponseEntity.status(401).body("Please log in first.");
        }
        return ResponseEntity.ok(researchStockService.getStockResearch(ticker));
    }
}