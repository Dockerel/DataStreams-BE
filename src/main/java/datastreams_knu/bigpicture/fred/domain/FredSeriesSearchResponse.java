package datastreams_knu.bigpicture.fred.domain;

import datastreams_knu.bigpicture.common.domain.BaseEntity;
import lombok.Data;

import java.util.*;

@Data
public class FredSeriesSearchResponse extends BaseEntity {
    private String realtimeStart;
    private String realtimeEnd;
    private String orderBy;
    private String sortOrder;
    private int count;
    private int offset;
    private int limit;
    private List<Series> seriess;

    @Data
    public static class Series {
        private String id;
        private String realtimeStart;
        private String realtimeEnd;
        private String title;
        private String observationStart;
        private String observationEnd;
        private String frequency;
        private String frequencyShort;
        private String units;
        private String unitsShort;
        private String seasonalAdjustment;
        private String seasonalAdjustmentShort;
        private String lastUpdated;
        private int popularity;
        private int groupPopularity;
        private String notes;
    }
}
