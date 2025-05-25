package datastreams_knu.bigpicture.alert.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FcmTestRequest {
    private String targetToken;
    private String title;
    private String body;

    @Builder
    public FcmTestRequest(String targetToken, String title, String body) {
        this.targetToken = targetToken;
        this.title = title;
        this.body = body;
    }

    public static FcmTestRequest of(String targetToken, String title, String body) {
        return FcmTestRequest.builder()
                .targetToken(targetToken)
                .title(title)
                .body(body)
                .build();
    }
}
