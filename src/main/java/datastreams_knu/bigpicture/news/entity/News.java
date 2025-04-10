package datastreams_knu.bigpicture.news.entity;

import datastreams_knu.bigpicture.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class News extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String keyword;

    @Lob
    private String content;

    @OneToMany(mappedBy = "news", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Reference> references = new ArrayList<>();

    public void addReference(Reference reference) {
        references.add(reference);
        reference.setNews(this);
    }

    @Builder
    public News(String keyword, String content) {
        this.keyword = keyword;
        this.content = content;
    }

    public static News of(String keyword, String content) {
        return News.builder()
            .keyword(keyword)
            .content(content)
            .build();
    }

    public static News of(String keyword) {
        return News.of(keyword, null);
    }

    public void setContent(String content) {
        this.content = content;
    }
}
