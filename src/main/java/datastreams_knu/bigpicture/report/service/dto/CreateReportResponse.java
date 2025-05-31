package datastreams_knu.bigpicture.report.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CreateReportResponse {
    private String title;
    private String summaryReport;
    private String economyReport;
    private String stockReport;

    @Builder
    public CreateReportResponse(String title, String summaryReport, String economyReport, String stockReport) {
        this.title = title;
        this.summaryReport = summaryReport;
        this.economyReport = economyReport;
        this.stockReport = stockReport;
    }

    public static CreateReportResponse of(String title, String summaryReport, String economyReport, String stockReport) {
        return CreateReportResponse.builder()
                .title(title)
                .summaryReport(summaryReport)
                .economyReport(economyReport)
                .stockReport(stockReport)
                .build();
    }
}
