package datastreams_knu.bigpicture.new_report.service.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ReportGenerationParams {
    // economy, stock
    String reportType;
    // reportType이 economy인 경우는 "", stock인 경우는 stockName(주식명 or 티커)
    String stockName;

    // 위험 수용 성향
    String riskTolerance;
    // 레포트 난이도
    String reportDifficultyLevel;
    // 관심 분야
    List<String> interestAreas;

    @Builder
    public ReportGenerationParams(String reportType, String stockName, String riskTolerance, String reportDifficultyLevel, List<String> interestAreas) {
        this.reportType = reportType;
        this.stockName = stockName;
        this.riskTolerance = riskTolerance;
        this.reportDifficultyLevel = reportDifficultyLevel;
        this.interestAreas = interestAreas;
    }

    public static ReportGenerationParams of(String reportType, String stockName, String riskTolerance, String reportDifficultyLevel, List<String> interestAreas) {
        return ReportGenerationParams.builder()
                .reportType(reportType)
                .stockName(stockName)
                .riskTolerance(riskTolerance)
                .reportDifficultyLevel(reportDifficultyLevel)
                .interestAreas(interestAreas)
                .build();
    }
}
