package datastreams_knu.bigpicture.schedule.controller;

import datastreams_knu.bigpicture.common.domain.ApiResponse;
import datastreams_knu.bigpicture.schedule.controller.dto.RegisterCrawlingDataRequest;
import datastreams_knu.bigpicture.schedule.controller.dto.RegisterCrawlingDataResponse;
import datastreams_knu.bigpicture.schedule.service.SchedulerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/schedule")
@RestController
public class ScheduleController {

    private final SchedulerService schedulerService;

    @GetMapping
    public ApiResponse<String> getTest() {
        return ApiResponse.ok("test ok");
    }

    @PostMapping("/register")
    public ApiResponse<RegisterCrawlingDataResponse> registerCrawlingData(@Valid @RequestBody RegisterCrawlingDataRequest request) {
        return ApiResponse.ok(schedulerService.registerCrawlingData(request.toServiceRequest()));
    }
}
