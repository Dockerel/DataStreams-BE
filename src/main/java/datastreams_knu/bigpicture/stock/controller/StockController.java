package datastreams_knu.bigpicture.stock.controller;

import datastreams_knu.bigpicture.common.domain.ApiResponse;
import datastreams_knu.bigpicture.stock.controller.dto.StockResponse;
import datastreams_knu.bigpicture.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/stocks")
@RestController
public class StockController {

    private final StockService stockService;

    @GetMapping
    public ApiResponse<List<StockResponse>> getStocks(@RequestParam("stockName") String stockName) {
        return ApiResponse.ok(stockService.getStocks(stockName));
    }
}
