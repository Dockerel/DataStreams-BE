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
    private String exchangesString;
    private String koreaInterestsString;
    private String usInterestsString;
    private String stockInfosString;

    @Builder
    public CreateReportResponse(String title, String summaryReport, String economyReport, String stockReport,
                                String exchangesString, String koreaInterestsString, String usInterestsString, String stockInfosString) {
        this.title = title;
        this.summaryReport = summaryReport;
        this.economyReport = economyReport;
        this.stockReport = stockReport;
        this.exchangesString = exchangesString;
        this.koreaInterestsString = koreaInterestsString;
        this.usInterestsString = usInterestsString;
        this.stockInfosString = stockInfosString;
    }

    public static CreateReportResponse of(String title, String summaryReport, String economyReport, String stockReport,
                                          String exchangesString, String koreaInterestsString, String usInterestsString, String stockInfosString) {
        return CreateReportResponse.builder()
                .title(title)
                .summaryReport(summaryReport)
                .economyReport(economyReport)
                .stockReport(stockReport)
                .exchangesString(exchangesString)
                .koreaInterestsString(koreaInterestsString)
                .usInterestsString(usInterestsString)
                .stockInfosString(stockInfosString)
                .build();
    }
}
