package datastreams_knu.bigpicture.news.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String keyword;

    private LocalDate newsCrawlingDate;

    @Lob
    private String content;

    @OneToMany(mappedBy = "news", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Reference> references = new ArrayList<>();

    public void addReference(Reference reference) {
        references.add(reference);
        reference.setNews(this);
    }

    @Builder
    public News(LocalDate newsCrawlingDate, String keyword, String content) {
        this.newsCrawlingDate = newsCrawlingDate;
        this.keyword = keyword;
        this.content = content;
    }

    public static News of(LocalDate newsCrawlingDate, String keyword, String content) {
        return News.builder()
            .newsCrawlingDate(newsCrawlingDate)
            .keyword(keyword)
            .content(content)
            .build();
    }

    public void setContent(String content) {
        this.content = content;
    }
}
