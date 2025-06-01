package datastreams_knu.bigpicture.common.util;

import datastreams_knu.bigpicture.common.util.dto.StockKeywordResolverResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Component
public class StockKeywordResolver {

    @Value("${python.server.url}")
    private String pythonServerUrl;

    private final WebClientUtil webClientUtil;

    public String resolve(String stockKeyword) {
        String encodedStockKeyword = URLEncoder.encode(stockKeyword, StandardCharsets.UTF_8);
        String url = pythonServerUrl + "/api/v1/stocks/resolve-keyword/" + encodedStockKeyword;
        StockKeywordResolverResponseDto response = webClientUtil.get(url, StockKeywordResolverResponseDto.class);
        if (response.getStatus().equals("400")) {
            throw new IllegalArgumentException("유효하지 않은 stockKeyword입니다.");
        }
        return response.getStockName();
    }
}
