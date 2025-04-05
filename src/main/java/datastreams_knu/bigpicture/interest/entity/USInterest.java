package datastreams_knu.bigpicture.interest.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Entity
@Table(name = "US_INTEREST")
public class USInterest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate interestDate;

    private Float interestRate;

    @Builder
    public USInterest(LocalDate interestDate, Float interestRate) {
        this.interestDate = interestDate;
        this.interestRate = interestRate;
    }

    public static USInterest of(LocalDate interestDate, Float interestRate) {
        return USInterest.builder()
            .interestDate(interestDate)
            .interestRate(interestRate)
            .build();
    }
}
