package datastreams_knu.bigpicture.exchange.agent;

import datastreams_knu.bigpicture.common.dto.CrawlingResultDto;
import datastreams_knu.bigpicture.common.util.WebClientUtil;
import datastreams_knu.bigpicture.exchange.agent.dto.ExchangeCrawlingDto;
import datastreams_knu.bigpicture.exchange.agent.dto.ExchangeInfoDto;
import datastreams_knu.bigpicture.exchange.entity.Exchange;
import datastreams_knu.bigpicture.exchange.repository.ExchangeRepository;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ExchangeCrawlingAgent {

    private final WebClientUtil webClientUtil;
    private final ExchangeRepository exchangeRepository;

    @Value("${exchange.api.base-url}")
    private String exchangeBaseUrl;

    @Tool("지난 일주일 동안의 환율 데이터를 수집합니다.")
    public List<ExchangeInfoDto> crawlingExchangeRate() {
        ExchangeCrawlingDto response = webClientUtil.get(exchangeBaseUrl, ExchangeCrawlingDto.class);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return response.getData().stream()
                .map(data -> {
                    LocalDate exchangeDate = LocalDate.parse(data.getDate(), formatter);
                    double exchangeRate = Double.parseDouble(data.getRate());
                    return ExchangeInfoDto.of(exchangeDate, exchangeRate);
                })
                .collect(Collectors.toList());
    }

    @Tool("수집된 환율 데이터의 평균값을 DB에 저장합니다.")
    public CrawlingResultDto saveExchange(List<ExchangeInfoDto> infos) {
        double averageExchangeRate = infos.stream()
                .mapToDouble(ExchangeInfoDto::getRate)
                .average()
                .orElse(0.0);
        double roundAverageExchangeRate = Math.round(averageExchangeRate * 100) / 100.0;
        exchangeRepository.save(Exchange.of(LocalDate.now(), roundAverageExchangeRate));
        return CrawlingResultDto.of(true, "환율 크롤링 성공");
    }
}
