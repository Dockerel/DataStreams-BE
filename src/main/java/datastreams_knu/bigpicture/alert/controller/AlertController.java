package datastreams_knu.bigpicture.alert.controller;

import datastreams_knu.bigpicture.alert.controller.dto.DeleteWatchlistRequest;
import datastreams_knu.bigpicture.alert.controller.dto.GetMyWatchlistRequest;
import datastreams_knu.bigpicture.alert.controller.dto.RegisterFcmTokenRequest;
import datastreams_knu.bigpicture.alert.controller.dto.RegisterWatchlistRequest;
import datastreams_knu.bigpicture.alert.service.AlertService;
import datastreams_knu.bigpicture.common.domain.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/alert")
@RequiredArgsConstructor
@RestController
public class AlertController {

    private final AlertService alertService;

    @PostMapping("/register-fcm-token")
    public ApiResponse<String> registerFcmToken(@RequestBody RegisterFcmTokenRequest request) {
        return ApiResponse.ok(alertService.registerFcmToken(request.toServiceRequest()));
    }

    @GetMapping
    public ApiResponse<List<String>> getMyWatchlist(@RequestBody GetMyWatchlistRequest request) {
        return ApiResponse.ok(alertService.getMyWatchlist(request.toServiceRequest()));
    }

    @PostMapping
    public ApiResponse<String> registerWatchlist(@RequestBody RegisterWatchlistRequest request) {
        return ApiResponse.ok(alertService.registerWatchlist(request.toServiceRequest()));
    }

    @DeleteMapping
    public ApiResponse<String> deleteWatchlist(@RequestBody DeleteWatchlistRequest request) {
        return ApiResponse.ok(alertService.deleteWatchlist(request.toServiceRequest()));
    }
}
