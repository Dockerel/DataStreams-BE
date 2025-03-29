package datastreams_knu.bigpicture.krx.service;

import datastreams_knu.bigpicture.common.config.WebClientConfig;
import datastreams_knu.bigpicture.common.domain.ApiResponse;
import datastreams_knu.bigpicture.krx.domain.KrxSeriesDailyPriceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class KrxApiService {

    private final WebClientConfig webClientConfig;

    @Value("${krx.api.base-url}")
    private String baseUrl;

    @Value("${krx.api.key}")
    private String apiKey;

    public ApiResponse<KrxSeriesDailyPriceResponse> getKrxSeriesDailyPrice(String basDd) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/krx_dd_trd")
                .queryParam("basDd", basDd)
                .queryParam("apiKey", apiKey)
                .build()
                .toUriString();

        KrxSeriesDailyPriceResponse response = webClientConfig.webClient().get()
                .uri(url)
                .retrieve()
                .bodyToMono(KrxSeriesDailyPriceResponse.class)
                .block();

        return ApiResponse.ok(response);
    }
}
