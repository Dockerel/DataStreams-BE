package datastreams_knu.bigpicture.interest.controller;

import datastreams_knu.bigpicture.common.domain.ApiResponse;
import datastreams_knu.bigpicture.interest.controller.dto.InterestResponse;
import datastreams_knu.bigpicture.interest.service.InterestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/interests")
@RequiredArgsConstructor
@RestController
@Tag(name = "금리", description = "금리 관련 API")
public class InterestController {

    private final InterestService interestService;

    @GetMapping("/korea")
    @Operation(summary = "한국 금리 데이터 조회", description = "모든 한국 금리 데이터 조회")
    public ApiResponse<List<InterestResponse>> getKoreaInterests() {
        return ApiResponse.ok(interestService.getKoreaInterests());
    }

    @GetMapping("/us")
    @Operation(summary = "미국 금리 데이터 조회", description = "모든 미국 금리 데이터 조회")
    public ApiResponse<List<InterestResponse>> getUSInterests() {
        return ApiResponse.ok(interestService.getUSInterests());
    }
}
