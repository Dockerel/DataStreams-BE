package datastreams_knu.bigpicture.report.service.dto;

import datastreams_knu.bigpicture.exchange.entity.Exchange;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ExchangePromptInputDto {
    private LocalDate exchangeDate;
    private double exchangeRate;

    @Builder
    public ExchangePromptInputDto(LocalDate exchangeDate, double exchangeRate) {
        this.exchangeDate = exchangeDate;
        this.exchangeRate = exchangeRate;
    }

    public static ExchangePromptInputDto from(Exchange exchange) {
        return ExchangePromptInputDto.builder()
            .exchangeDate(exchange.getExchangeDate())
            .exchangeRate(exchange.getExchangeRate())
            .build();
    }
}
