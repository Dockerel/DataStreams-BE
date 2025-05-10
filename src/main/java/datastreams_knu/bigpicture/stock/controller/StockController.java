package datastreams_knu.bigpicture.stock.controller;

import datastreams_knu.bigpicture.common.domain.ApiResponse;
import datastreams_knu.bigpicture.stock.controller.dto.StockResponse;
import datastreams_knu.bigpicture.stock.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/stocks")
@RestController
@Tag(name = "주가", description = "주가 관련 API")
public class StockController {

    private final StockService stockService;

    @GetMapping
    @Operation(summary = "주가 데이터 조회", description = "모든 주가 데이터 조회")
    public ApiResponse<List<StockResponse>> getStocks(@RequestParam("stockName") String stockName) {
        return ApiResponse.ok(stockService.getStocks(stockName));
    }
}
