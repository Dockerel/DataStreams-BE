package datastreams_knu.bigpicture.common.util;

import datastreams_knu.bigpicture.common.config.WebClientConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebClientUtil {

    private final WebClientConfig webClientConfig;

    public <T> T get(String url, Class<T> clazz) {
        return webClientConfig.webClient().get()
            .uri(url)
            .retrieve()
            .bodyToMono(clazz)
            .block();
    }
}
