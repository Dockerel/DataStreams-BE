package datastreams_knu.bigpicture.schedule.scheduler;

import datastreams_knu.bigpicture.common.dto.DeleteResultDto;
import datastreams_knu.bigpicture.schedule.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ExpireDataDeleteScheduler {

    private final SchedulerService schedulerService;

    // 달러 환율 크롤링 데이터 삭제
    @Scheduled(cron = "0 15 1 * * MON", zone = "Asia/Seoul") // 월요일 새벽 1시 15분
    public void runExchangeCrawling() {
        DeleteResultDto result = schedulerService.deleteExpireExchangeData();
        log.info("{}: {}", result.getMessage(), result.getDeleteCount());
    }
}
