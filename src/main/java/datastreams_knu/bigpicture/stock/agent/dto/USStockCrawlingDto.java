package datastreams_knu.bigpicture.stock.agent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class USStockCrawlingDto {
    private List<Result> results;

    /**
     * c: 종가
     * t: 기준일자(ms 형태)
     */
    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class Result {
        private double c;
        private Long t;

        public static Result of(double c, Long t) {
            return Result.builder()
                .c(c)
                .t(t)
                .build();
        }
    }

    public static USStockCrawlingDto of(List<Result> results) {
        return USStockCrawlingDto.builder()
            .results(results)
            .build();
    }
}