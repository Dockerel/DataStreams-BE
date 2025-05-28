package datastreams_knu.bigpicture.exchange.controller.dto;

import datastreams_knu.bigpicture.exchange.entity.Exchange;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class ExchangeResponse {
    private LocalDate exchangeDate;
    private double exchangeRate;

    @Builder
    public ExchangeResponse(LocalDate exchangeDate, double exchangeRate) {
        this.exchangeDate = exchangeDate;
        this.exchangeRate = exchangeRate;
    }

    public static ExchangeResponse of(LocalDate date, double exchangeRate) {
        return ExchangeResponse.builder()
                .exchangeDate(date)
                .exchangeRate(exchangeRate)
                .build();
    }

    public static ExchangeResponse from(Exchange exchange) {
        return ExchangeResponse.of(exchange.getExchangeDate(), exchange.getExchangeRate());
    }
}
