package datastreams_knu.bigpicture.report.service.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateReportServiceRequest {
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
    public CreateReportServiceRequest(String reportType, String stockName, String riskTolerance, String reportDifficultyLevel, List<String> interestAreas) {
        this.reportType = reportType;
        this.stockName = stockName;
        this.riskTolerance = riskTolerance;
        this.reportDifficultyLevel = reportDifficultyLevel;
        this.interestAreas = interestAreas;
    }

    public static CreateReportServiceRequest of(String reportType, String stockName, String riskTolerance, String reportDifficultyLevel, List<String> interestAreas) {
        return CreateReportServiceRequest.builder()
                .reportType(reportType)
                .stockName(stockName)
                .riskTolerance(riskTolerance)
                .reportDifficultyLevel(reportDifficultyLevel)
                .interestAreas(interestAreas)
                .build();
    }
}
