package datastreams_knu.bigpicture.interest.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Entity
public class KoreaInterest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate interestDate;

    private Float interestRate;

    @Builder
    public KoreaInterest(LocalDate interestDate, Float interestRate) {
        this.interestDate = interestDate;
        this.interestRate = interestRate;
    }

    public static KoreaInterest of(LocalDate interestDate, Float interestRate) {
        return KoreaInterest.builder()
            .interestDate(interestDate)
            .interestRate(interestRate)
            .build();
    }
}
