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
public class CrawlingInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stockType; // korea, us
    private String stockName; // name / ticker

    private String newsKeyword; // news keyword

    @Builder
    public CrawlingInfo(String stockType, String stockName, String newsKeyword) {
        this.stockType = stockType;
        this.stockName = stockName;
        this.newsKeyword = newsKeyword;
    }

    public static CrawlingInfo of(String stockType, String stockName, String newsKeyword) {
        return CrawlingInfo.builder()
            .stockType(stockType)
            .stockName(stockName)
            .newsKeyword(newsKeyword)
            .build();
    }
}
