package datastreams_knu.bigpicture.interest.controller;

import datastreams_knu.bigpicture.common.domain.ApiResponse;
import datastreams_knu.bigpicture.interest.controller.dto.InterestResponse;
import datastreams_knu.bigpicture.interest.service.InterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/interests")
@RequiredArgsConstructor
@RestController
public class InterestController {

    private final InterestService interestService;

    @GetMapping("/korea")
    public ApiResponse<List<InterestResponse>> getKoreaInterests() {
        return ApiResponse.ok(interestService.getKoreaInterests());
    }

    @GetMapping("/us")
    public ApiResponse<List<InterestResponse>> getUSInterests() {
        return ApiResponse.ok(interestService.getUSInterests());
    }
}
