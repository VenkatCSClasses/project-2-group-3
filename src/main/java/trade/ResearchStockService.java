package trade;

import org.springframework.stereotype.Service;

@Service
public class ResearchStockService {

    public ResearchStock getStockResearch(String ticker) {
        ResearchStock stock = new ResearchStock();

        stock.setTicker(ticker.toUpperCase());
        stock.setCompanyName("Apple Inc.");
        stock.setLastClosingPrice(210.15);
        stock.setLastOpeningPrice(208.40);
        stock.setVolume(58234120);

        stock.setOneDayPriceChange(1.75);
        stock.setOneDayPercentChange(0.84);

        stock.setOneWeekPriceChange(4.20);
        stock.setOneWeekPercentChange(2.04);

        stock.setOneMonthPriceChange(9.35);
        stock.setOneMonthPercentChange(4.66);

        stock.setThreeMonthPriceChange(18.10);
        stock.setThreeMonthPercentChange(9.42);

        stock.setSixMonthPriceChange(26.55);
        stock.setSixMonthPercentChange(14.46);

        stock.setYearToDatePriceChange(22.30);
        stock.setYearToDatePercentChange(11.87);

        return stock;
    }
}