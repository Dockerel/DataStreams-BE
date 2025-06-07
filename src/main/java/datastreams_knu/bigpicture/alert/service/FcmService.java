package datastreams_knu.bigpicture.alert.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import datastreams_knu.bigpicture.alert.service.dto.FcmRequest;
import datastreams_knu.bigpicture.common.exception.ObjectMapperException;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FcmService {

    public final String MEDIA_TYPE = "application/json; charset=utf-8";
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/bigpicture-c2798/messages:send";

    private final ObjectMapper objectMapper;

    public void sendMessageTo(String fcmToken, String title, String body) {
        String message = makeMessage(fcmToken, title, body);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message,
                MediaType.get(MEDIA_TYPE));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, MEDIA_TYPE)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("FCM 요청 실패: " + response.code() + " - " + response.message());
            }
        } catch (IOException e) {
            throw new UncheckedIOException("FCM 메시지 전송 중 I/O 오류가 발생했습니다", e);
        }
    }

    private String makeMessage(String targetToken, String title, String body) {
        FcmRequest fcmMessage = FcmRequest.builder()
                .message(FcmRequest.Message.builder()
                        .token(targetToken)
                        .notification(FcmRequest.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();

        try {
            return objectMapper.writeValueAsString(fcmMessage);
        } catch (JsonProcessingException e) {
            throw new ObjectMapperException("직렬화 중 예외가 발생하였습니다.", e);
        }
    }

    private String getAccessToken() {
        try {
            String firebaseConfigPath = "firebase/firebase_service_key.json";

            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                    .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

            googleCredentials.refreshIfExpired();
            return googleCredentials.getAccessToken().getTokenValue();
        } catch (IOException e) {
            throw new UncheckedIOException("FCM AccessToken을 받아오지 못했습니다.", e);
        }
    }
}
