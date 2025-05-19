package datastreams_knu.bigpicture.alert.controller;

import datastreams_knu.bigpicture.alert.controller.dto.FcmTestRequest;
import datastreams_knu.bigpicture.alert.service.FcmService;
import datastreams_knu.bigpicture.common.domain.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class FcmTestController {

    private final FcmService fcmService;

    @PostMapping("/api/v1/fcm")
    public ApiResponse<String> pushMessage(@RequestBody FcmTestRequest request) throws IOException {
        System.out.println(request.getTargetToken() + " "
                + request.getTitle() + " " + request.getBody());

        fcmService.sendMessageTo(
                request.getTargetToken(),
                request.getTitle(),
                request.getBody());
        return ApiResponse.ok("메시지가 전송되었습니다");
    }
}
