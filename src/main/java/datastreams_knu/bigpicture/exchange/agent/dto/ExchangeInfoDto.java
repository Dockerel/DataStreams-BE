package datastreams_knu.bigpicture.exchange.agent.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ExchangeInfoDto {
    public LocalDate date;
    public double rate;

    @Builder
    public ExchangeInfoDto(LocalDate date, double rate) {
        this.date = date;
        this.rate = rate;
    }

    public static ExchangeInfoDto of(LocalDate date, double rate) {
        return ExchangeInfoDto.builder()
                .date(date)
                .rate(rate)
                .build();
    }
}
