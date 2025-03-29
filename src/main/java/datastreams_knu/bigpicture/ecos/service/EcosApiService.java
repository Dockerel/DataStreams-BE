package datastreams_knu.bigpicture.ecos.service;

import datastreams_knu.bigpicture.common.domain.ApiResponse;
import datastreams_knu.bigpicture.common.util.WebClientUtil;
import java.util.Map;

import datastreams_knu.bigpicture.ecos.dto.KeyStatisticDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EcosApiService {

    @Value("${ecos.api.key}")
    private String apiKey;

    @Value("${ecos.api.url}")
    private String apiUrl;

    private final WebClientUtil webClientUtil;

    public ApiResponse<List<KeyStatisticDto>> getKeyStatistics() {
        String url = String.format("%s/%s/json/kr/1/100/", apiUrl, apiKey);
        Map response = webClientUtil.get(url, Map.class);
        List<Map<String, Object>> rawData = (List<Map<String, Object>>)
                ((Map) response.get("KeyStatisticList")).get("row");

        List<KeyStatisticDto> result = rawData.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ApiResponse.ok(result);
    }

    private KeyStatisticDto convertToDto(Map<String, Object> item) {
        return KeyStatisticDto.builder()
                .code((String) item.get("KEYSTAT_CODE"))
                .name((String) item.get("KEYSTAT_NAME"))
                .value((String) item.get("DATA_VALUE"))
                .cycle((String) item.get("CYCLE"))
                .unit((String) item.get("UNIT_NAME"))
                .date((item.get("TIME") != null) ? java.time.LocalDate.parse((String) item.get("TIME")) : null)
                .build();
    }
}
