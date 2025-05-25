package datastreams_knu.bigpicture.alert.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RegisterFcmTokenServiceRequest {
    private String uuid;
    private String fcmToken;

    @Builder
    public RegisterFcmTokenServiceRequest(String uuid, String fcmToken) {
        this.uuid = uuid;
        this.fcmToken = fcmToken;
    }

    public static RegisterFcmTokenServiceRequest of(String uuid, String fcmToken) {
        return RegisterFcmTokenServiceRequest.builder()
                .uuid(uuid)
                .fcmToken(fcmToken)
                .build();
    }
}
