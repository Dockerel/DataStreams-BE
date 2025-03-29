package datastreams_knu.bigpicture.ecos;

import datastreams_knu.bigpicture.common.domain.ApiResponse;
import datastreams_knu.bigpicture.ecos.dto.KeyStatisticDto;
import datastreams_knu.bigpicture.ecos.service.EcosApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureWebClient
public class EcosApiServiceIntegrationTest {

    @Autowired
    private EcosApiService ecosApiService;

    @Autowired
    private WebClient webClient;

    @Value("${ecos.api.url}")
    private String apiUrl;

    @Value("${ecos.api.key}")
    private String apiKey;

    @Test
    void getKeyStatistics_LiveApiCall_ReturnsList() {
        // API 호출

        ApiResponse<List<KeyStatisticDto>> result = ecosApiService.getKeyStatistics();

        // 결과 검증
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatus());
        assertFalse(result.getData().isEmpty());
    }
}
