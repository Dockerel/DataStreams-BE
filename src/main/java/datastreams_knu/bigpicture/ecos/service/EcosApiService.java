package datastreams_knu.bigpicture.ecos.service;

import datastreams_knu.bigpicture.common.domain.ApiResponse;
import datastreams_knu.bigpicture.ecos.domain.EcosKeyStatisticResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.netty.http.client.HttpClient;

import java.util.List;
import java.util.Map;

@Service
public class EcosApiService {

    private final WebClient webClient;
    private final String baseUrl;
    private final String apiKey;

    public EcosApiService(
            @Value("${ecos.api.base-url}") String baseUrl,
            @Value("${ecos.api.key}") String apiKey) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;

        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create().followRedirect(true)
                ))
                .build();
    }

    public ApiResponse<List<EcosKeyStatisticResponse.KeyStatistic>> getKeyStatistics() {
        String url = UriComponentsBuilder.fromPath("/")
                .path(apiKey)
                .path("/json/kr/1/100/")
                .build()
                .toUriString();

        Map response = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        // API 응답에서 KeyStatisticList -> row 데이터 추출
        Map<String, Object> keyStatisticList = (Map<String, Object>) response.get("KeyStatisticList");
        List<Map<String, Object>> rawData = (List<Map<String, Object>>) keyStatisticList.get("row");

        // EcosKeyStatisticResponse 객체 생성
        EcosKeyStatisticResponse ecosResponse = new EcosKeyStatisticResponse();
        ecosResponse.setCount(rawData.size());
        ecosResponse.setRequestDate(java.time.LocalDate.now().toString());

        // KeyStatistic 객체 리스트 생성
        List<EcosKeyStatisticResponse.KeyStatistic> statistics = rawData.stream()
                .map(item -> {
                    EcosKeyStatisticResponse.KeyStatistic statistic = new EcosKeyStatisticResponse.KeyStatistic();
                    statistic.setStat_code((String) item.get("KEYSTAT_CODE"));
                    statistic.setStat_name((String) item.get("KEYSTAT_NAME"));
                    statistic.setStat_value((String) item.get("DATA_VALUE"));
                    statistic.setStat_cycle((String) item.get("CYCLE"));
                    statistic.setStat_unit((String) item.get("UNIT_NAME"));
                    statistic.setStat_date((String) item.get("TIME"));
                    return statistic;
                })
                .toList();

        ecosResponse.setRow(statistics);

        return ApiResponse.ok(statistics);
    }
}
