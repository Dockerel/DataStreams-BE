package datastreams_knu.bigpicture.exchange.controller;

import datastreams_knu.bigpicture.common.domain.ApiResponse;
import datastreams_knu.bigpicture.exchange.controller.dto.ExchangeResponse;
import datastreams_knu.bigpicture.exchange.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/exchanges")
@RequiredArgsConstructor
@RestController
public class ExchangeController {

    private final ExchangeService exchangeService;

    @GetMapping
    public ApiResponse<List<ExchangeResponse>> getExchanges() {
        return ApiResponse.ok(exchangeService.getExchanges());
    }
}
