package datastreams_knu.bigpicture.exchange.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Entity
public class Exchange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate exchangeDate;
    private double exchangeRate;

    @Builder
    public Exchange(LocalDate exchangeDate, double exchangeRate) {
        this.exchangeDate = exchangeDate;
        this.exchangeRate = exchangeRate;
    }

    public static Exchange of(LocalDate exchangeDate, double exchangeRate) {
        return Exchange.builder()
                .exchangeDate(exchangeDate)
                .exchangeRate(exchangeRate)
                .build();
    }
}
