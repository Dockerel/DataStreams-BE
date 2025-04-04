package datastreams_knu.bigpicture.stock.agent.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class USStockCrawlingDto {
    private List<Result> results;

    /**
     * c: 종가
     * t: 기준일자(ms 형태)
     */
    @Getter
    public static class Result {
        private double c;
        private Long t;
    }
}