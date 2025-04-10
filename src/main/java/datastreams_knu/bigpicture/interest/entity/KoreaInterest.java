package datastreams_knu.bigpicture.interest.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
public class KoreaInterest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate interestDate;

    private double interestRate;

    @Builder
    public KoreaInterest(LocalDate interestDate, double interestRate) {
        this.interestDate = interestDate;
        this.interestRate = interestRate;
    }

    public static KoreaInterest of(LocalDate interestDate, double interestRate) {
        return KoreaInterest.builder()
            .interestDate(interestDate)
            .interestRate(interestRate)
            .build();
    }
}
