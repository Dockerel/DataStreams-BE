package datastreams_knu.bigpicture.fred.domain;

import datastreams_knu.bigpicture.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "fred_series_search")
public class FredSeriesSearchResponse extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "realtime_start") // 데이터 수집 시작, 종료 날짜
    private String realtimeStart;

    @Column(name = "realtime_end")
    private String realtimeEnd;

    @Column(name = "order_by") // 정렬 기준
    private String orderBy;

    @Column(name = "sort_order") // 정렬 방식 (asc/desc)
    private String sortOrder;

    @Column(name = "count") // 총 결과 수
    private int count;

    @Column(name = "offset") // 페이징 시작 위치
    private int offset;

    @Column(name = "limit") // 한 번에 가져올 최대 결과 수
    private int limit;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "fred_series_search_id") // 외래키 설정
    private List<Series> seriess;

    @Getter
    @Setter
    @Entity
    @Table(name = "fred_series_search_series")
    public static class Series {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "series_table_id")
        private Long seriesTableId; // 테이블 내 ID

        @Column(name = "series_id", nullable = false)
        private String seriesId; // Series 고유 ID (FRED 제공)

        @Column(name = "realtime_start")
        private String realtimeStart;

        @Column(name = "realtime_end")
        private String realtimeEnd;

        @Column(name = "title")
        private String title;

        @Column(name = "observation_start")
        private String observationStart;

        @Column(name = "observation_end")
        private String observationEnd;

        @Column(name = "frequency") // 데이터 빈도 (월별, 분기별 등)
        private String frequency;

//        @Column(name = "frequency_short")
//        private String frequencyShort;

        @Column(name = "units") // 데이터 단위 (달러, 퍼센트 등)
        private String units;

//        @Column(name = "units_short")
//        private String unitsShort;

        @Column(name = "seasonal_adjustment")
        private String seasonalAdjustment;

//        @Column(name = "seasonal_adjustment_short")
//        private String seasonalAdjustmentShort;

        @Column(name = "last_updated")
        private String lastUpdated;

        @Column(name = "popularity") // 인기 지표
        private int popularity;

        @Column(name = "group_popularity") // 그룹 내 인기 지표
        private int groupPopularity;

//        @Lob
//        @Column(name = "notes", columnDefinition = "TEXT") // 긴 텍스트 처리
//        private String notes;
    }
}