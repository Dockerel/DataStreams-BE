package datastreams_knu.bigpicture.report.facade;

import datastreams_knu.bigpicture.report.service.ReportService;
import datastreams_knu.bigpicture.report.service.dto.CreateReportResponse;
import datastreams_knu.bigpicture.report.service.dto.CreateReportServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportFacade {

    private final ReportService reportService;

    public CreateReportResponse getReport(CreateReportServiceRequest request) {
        CreateReportResponse report = reportService.createReport(request);
        return report;
    }
}
