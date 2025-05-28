package datastreams_knu.bigpicture.report.service.dto;

import datastreams_knu.bigpicture.interest.entity.KoreaInterest;
import datastreams_knu.bigpicture.interest.entity.USInterest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class InterestPromptInputDto {
    private LocalDate interestDate;
    private double interestRate;

    @Builder
    public InterestPromptInputDto(LocalDate interestDate, double interestRate) {
        this.interestDate = interestDate;
        this.interestRate = interestRate;
    }

    public static InterestPromptInputDto from(KoreaInterest interest) {
        return InterestPromptInputDto.builder()
                .interestDate(interest.getInterestDate())
                .interestRate(interest.getInterestRate())
                .build();
    }

    public static InterestPromptInputDto from(USInterest interest) {
        return InterestPromptInputDto.builder()
                .interestDate(interest.getInterestDate())
                .interestRate(interest.getInterestRate())
                .build();
    }
}
