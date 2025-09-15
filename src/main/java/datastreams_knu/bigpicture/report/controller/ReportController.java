package datastreams_knu.bigpicture.report.controller;

import datastreams_knu.bigpicture.common.domain.ApiResponse;
import datastreams_knu.bigpicture.report.controller.dto.CreateReportRequest;
import datastreams_knu.bigpicture.report.facade.ReportFacade;
import datastreams_knu.bigpicture.report.service.dto.CreateReportResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
@RestController
@Tag(name = "레포트", description = "레포트 관련 API")
public class ReportController {

    private final ReportFacade reportFacade;

    @PostMapping
    @Operation(summary = "레포트 생성", description = "레포트 생성")
    public ApiResponse<CreateReportResponse> createReport(@RequestBody CreateReportRequest request) {
        return ApiResponse.ok(reportFacade.getReport(request.toServiceRequest()));
    }
}
