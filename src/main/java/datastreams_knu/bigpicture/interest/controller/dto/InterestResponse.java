package datastreams_knu.bigpicture.interest.controller.dto;

import datastreams_knu.bigpicture.interest.entity.KoreaInterest;
import datastreams_knu.bigpicture.interest.entity.USInterest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class InterestResponse {
    private LocalDate interestDate;
    private double interestRate;

    @Builder
    public InterestResponse(LocalDate interestDate, double interestRate) {
        this.interestDate = interestDate;
        this.interestRate = interestRate;
    }

    public static InterestResponse of(LocalDate interestDate, double interestRate) {
        return InterestResponse.builder()
            .interestDate(interestDate)
            .interestRate(interestRate)
            .build();
    }

    public static InterestResponse from(KoreaInterest interest) {
        return InterestResponse.of(interest.getInterestDate(), interest.getInterestRate());
    }

    public static InterestResponse from(USInterest interest) {
        return InterestResponse.of(interest.getInterestDate(), interest.getInterestRate());
    }
}
