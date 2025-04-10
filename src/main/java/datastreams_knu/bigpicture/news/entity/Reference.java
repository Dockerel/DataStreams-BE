package datastreams_knu.bigpicture.news.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Reference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;
    private String date;

    @ManyToOne
    @JoinColumn(name = "NEWS_ID")
    private News news;

    @Builder
    public Reference(String url, String date) {
        this.url = url;
        this.date = date;
    }

    public static Reference of(String url, String date) {
        return Reference.builder()
            .url(url)
            .date(date)
            .build();
    }

    public void setNews(News news) {
        this.news = news;
    }
}
