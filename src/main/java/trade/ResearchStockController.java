package trade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stocks")
public class ResearchStockController {

    private final ResearchStockService researchStockService;

    @Autowired
    public ResearchStockController(ResearchStockService researchStockService) {
        this.researchStockService = researchStockService;
    }

    @GetMapping("/{ticker}")
    public ResearchStock getStock(@PathVariable String ticker) {
        return researchStockService.getStockResearch(ticker);
    }
}