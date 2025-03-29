package datastreams_knu.bigpicture.fred.service;

import datastreams_knu.bigpicture.common.domain.ApiResponse;
import datastreams_knu.bigpicture.fred.domain.FredSeriesSearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.netty.http.client.HttpClient;
import datastreams_knu.bigpicture.common.config.WebClientConfig;
import lombok.RequiredArgsConstructor;

@Service
public class FredApiService {

    private final WebClient webClient;
    private final String baseUrl;
    private final String apiKey;

    public FredApiService(
            @Value("${fred.api.base-url}") String baseUrl,
            @Value("${fred.api.key}") String apiKey) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;

        // 버퍼 크기 10MB로 증가
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
                .build();

        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .exchangeStrategies(strategies)
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create().followRedirect(true)
                ))
                .build();
    }

    public ApiResponse<FredSeriesSearchResponse> searchSeries(String searchText) {
        String url = UriComponentsBuilder.fromPath("/series/search")
                .queryParam("search_text", searchText)
                .queryParam("api_key", apiKey)
                .queryParam("file_type", "json")
                .queryParam("exclude_notes", "True")
                .build()
                .toUriString();

        FredSeriesSearchResponse response = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(FredSeriesSearchResponse.class)
                .block();

        return ApiResponse.ok(response);
    }
}