package datastreams_knu.bigpicture.report.controller.dto;

import datastreams_knu.bigpicture.report.service.dto.CreateReportServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class CreateReportRequest {
    // economy, stock
    String reportType;
    // reportType이 economy인 경우는 "", stock인 경우는 stockName(주식명 or 티커)
    String stockName;

    @Builder
    public CreateReportRequest(String reportType, String stockName) {
        this.reportType = reportType;
        this.stockName = stockName;
    }

    public CreateReportServiceRequest toServiceRequest() {
        return CreateReportServiceRequest.builder()
                .reportType(reportType)
                .stockName(stockName)
                .build();
    }
}