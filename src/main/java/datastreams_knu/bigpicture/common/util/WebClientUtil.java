package datastreams_knu.bigpicture.common.util;

import datastreams_knu.bigpicture.common.config.WebClientConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class WebClientUtil {

    private final WebClientConfig webClientConfig;

    public <T> T get(String url, Class<T> clazz) {
        return webClientConfig.webClient().get()
            .uri(url)
            .retrieve()
            .bodyToMono(clazz)
            .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)))
            .block();
    }
}
