package datastreams_knu.bigpicture.alert.controller.dto;

import datastreams_knu.bigpicture.alert.service.dto.RegisterFcmTokenServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegisterFcmTokenRequest {
    private String uuid;
    private String fcmToken;

    @Builder
    public RegisterFcmTokenRequest(String uuid, String fcmToken) {
        this.uuid = uuid;
        this.fcmToken = fcmToken;
    }

    public static RegisterFcmTokenRequest of(String uuid, String fcmToken) {
        return RegisterFcmTokenRequest.builder()
                .uuid(uuid)
                .fcmToken(fcmToken)
                .build();
    }

    public RegisterFcmTokenServiceRequest toServiceRequest() {
        return RegisterFcmTokenServiceRequest.of(uuid, fcmToken);
    }
}
