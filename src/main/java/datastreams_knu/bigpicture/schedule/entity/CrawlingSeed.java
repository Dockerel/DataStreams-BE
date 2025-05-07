package datastreams_knu.bigpicture.schedule.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class CrawlingSeed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stockType; // korea or us
    private String stockName; // name or ticker
    private String stockKeyword; // news keyword

    @Builder
    public CrawlingSeed(String stockType, String stockName, String stockKeyword) {
        this.stockType = stockType;
        this.stockName = stockName;
        this.stockKeyword = stockKeyword;
    }

    public static CrawlingSeed of(String stockType, String stockName, String newsKeyword) {
        return CrawlingSeed.builder()
            .stockType(stockType)
            .stockName(stockName)
            .stockKeyword(newsKeyword)
            .build();
    }
}
