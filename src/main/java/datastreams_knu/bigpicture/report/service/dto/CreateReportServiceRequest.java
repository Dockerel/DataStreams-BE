package datastreams_knu.bigpicture.report.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateReportServiceRequest {
    // economy, stock
    String reportType;
    // reportType이 economy인 경우는 "", stock인 경우는 stockName(주식명 or 티커)
    String stockName;

    @Builder
    public CreateReportServiceRequest(String reportType, String stockName) {
        this.reportType = reportType;
        this.stockName = stockName;
    }

    public static CreateReportServiceRequest of(String reportType, String stockName) {
        return CreateReportServiceRequest.builder()
                .reportType(reportType)
                .stockName(stockName)
                .build();
    }
}
