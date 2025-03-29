package datastreams_knu.bigpicture.krx.controller;

import datastreams_knu.bigpicture.common.domain.ApiResponse;
import datastreams_knu.bigpicture.krx.domain.KrxSeriesDailyPriceResponse;
import datastreams_knu.bigpicture.krx.service.KrxApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/krx")
@RequiredArgsConstructor
public class KrxApiController {

    private final KrxApiService krxApiService;

    @GetMapping("/series-daily-price")
    public ApiResponse<KrxSeriesDailyPriceResponse> getKrxSeriesDailyPrice(@RequestParam String basDd) {
        return krxApiService.getKrxSeriesDailyPrice(basDd);
    }
}
