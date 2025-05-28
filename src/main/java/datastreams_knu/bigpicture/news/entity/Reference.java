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

    @ManyToOne
    @JoinColumn(name = "NEWS_ID")
    private News news;

    @Builder
    public Reference(String url) {
        this.url = url;
    }

    public static Reference of(String url) {
        return Reference.builder()
                .url(url)
                .build();
    }

    protected void setNews(News news) {
        this.news = news;
    }
}
