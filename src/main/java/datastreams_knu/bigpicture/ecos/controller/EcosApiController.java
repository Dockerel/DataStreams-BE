package datastreams_knu.bigpicture.ecos.controller;

import datastreams_knu.bigpicture.common.domain.ApiResponse;
import datastreams_knu.bigpicture.ecos.domain.EcosKeyStatisticResponse;
import datastreams_knu.bigpicture.ecos.service.EcosApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ecos")
@RequiredArgsConstructor
public class EcosApiController {

    private final EcosApiService ecosService;

    @GetMapping("/key-statistics")
    public ApiResponse<List<EcosKeyStatisticResponse.KeyStatistic>> getKeyStatistics() {
        return ecosService.getKeyStatistics();
    }
}
