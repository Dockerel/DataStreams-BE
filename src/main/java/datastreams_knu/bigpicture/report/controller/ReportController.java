package datastreams_knu.bigpicture.report.controller;

import datastreams_knu.bigpicture.common.domain.ApiResponse;
import datastreams_knu.bigpicture.report.controller.dto.CreateReportRequest;
import datastreams_knu.bigpicture.report.service.ReportService;
import datastreams_knu.bigpicture.report.service.dto.ReportDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
@RestController
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ApiResponse<ReportDto> createReport(@RequestBody CreateReportRequest request) {
        return ApiResponse.ok(reportService.createReport(request.toServiceRequest()));
    }
}
