package datastreams_knu.bigpicture.new_report.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ReportGenerationResponse {
    private String title;
    private String summaryReport;
    private String economyReport;
    private String stockReport;

    @Builder
    public ReportGenerationResponse(String title, String summaryReport, String economyReport, String stockReport) {
        this.title = title;
        this.summaryReport = summaryReport;
        this.economyReport = economyReport;
        this.stockReport = stockReport;
    }

    public static ReportGenerationResponse of(String title, String summaryReport, String economyReport, String stockReport) {
        return ReportGenerationResponse.builder()
                .title(title)
                .summaryReport(summaryReport)
                .economyReport(economyReport)
                .stockReport(stockReport)
                .build();
    }
}
