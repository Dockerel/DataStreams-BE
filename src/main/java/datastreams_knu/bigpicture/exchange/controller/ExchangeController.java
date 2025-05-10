package datastreams_knu.bigpicture.exchange.controller;

import datastreams_knu.bigpicture.common.domain.ApiResponse;
import datastreams_knu.bigpicture.exchange.controller.dto.ExchangeResponse;
import datastreams_knu.bigpicture.exchange.service.ExchangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/exchanges")
@RequiredArgsConstructor
@RestController
@Tag(name = "환율", description = "환율 관련 API")
public class ExchangeController {

    private final ExchangeService exchangeService;

    @GetMapping
    @Operation(summary = "환율 데이터 조회", description = "모든 환율 데이터 조회")
    public ApiResponse<List<ExchangeResponse>> getExchanges() {
        return ApiResponse.ok(exchangeService.getExchanges());
    }
}
