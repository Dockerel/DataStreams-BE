package datastreams_knu.bigpicture.report.scheduler;

import datastreams_knu.bigpicture.report.controller.dto.CreateReportRequest;
import datastreams_knu.bigpicture.report.facade.ReportFacade;
import datastreams_knu.bigpicture.schedule.entity.CrawlingInfo;
import datastreams_knu.bigpicture.schedule.repository.CrawlingInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReportScheduler {

    private final ReportFacade reportFacade;
    private final CrawlingInfoRepository crawlingInfoRepository;

    @Scheduled(cron = "0 1 4 * * *", zone = "Asia/Seoul")
    public void runReportCreation() {
        List<CrawlingInfo> crawlingInfos = crawlingInfoRepository.findAll();

        for (CrawlingInfo crawlingInfo : crawlingInfos) {
            CreateReportRequest request = CreateReportRequest.of(crawlingInfo.getStockType(), crawlingInfo.getStockName());
            reportFacade.getReport(request.toServiceRequest());
        }
    }
}
