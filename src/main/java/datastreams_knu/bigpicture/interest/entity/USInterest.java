package datastreams_knu.bigpicture.interest.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "US_INTEREST")
public class USInterest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate interestDate;

    private double interestRate;

    @Builder
    public USInterest(LocalDate interestDate, double interestRate) {
        this.interestDate = interestDate;
        this.interestRate = interestRate;
    }

    public static USInterest of(LocalDate interestDate, double interestRate) {
        return USInterest.builder()
                .interestDate(interestDate)
                .interestRate(interestRate)
                .build();
    }
}
