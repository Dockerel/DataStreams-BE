package datastreams_knu.bigpicture.schedule.controller;

import datastreams_knu.bigpicture.common.domain.ApiResponse;
import datastreams_knu.bigpicture.schedule.controller.dto.RegisterCrawlingDataRequest;
import datastreams_knu.bigpicture.schedule.controller.dto.RegisterCrawlingDataResponse;
import datastreams_knu.bigpicture.schedule.service.SchedulerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/schedule")
@RestController
@Tag(name = "스케줄링", description = "스케줄링 관련 API")
public class ScheduleController {

    private final SchedulerService schedulerService;

    @PostMapping("/register")
    @Operation(summary = "크롤링 데이터 등록", description = "크롤링 데이터 등록")
    public ApiResponse<RegisterCrawlingDataResponse> registerCrawlingData(@Valid @RequestBody RegisterCrawlingDataRequest request) {
        return ApiResponse.ok(schedulerService.registerCrawlingData(request.toServiceRequest()));
    }
}
