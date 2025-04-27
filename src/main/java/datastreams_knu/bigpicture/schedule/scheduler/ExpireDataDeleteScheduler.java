package datastreams_knu.bigpicture.schedule.scheduler;

import datastreams_knu.bigpicture.common.dto.DeleteResultDto;
import datastreams_knu.bigpicture.schedule.service.ExpireDataDeleteSchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ExpireDataDeleteScheduler {

    private final ExpireDataDeleteSchedulerService schedulerService;

    // 달러 환율 크롤링 데이터 삭제
    @Scheduled(cron = "0 15 1 * * MON", zone = "Asia/Seoul") // 매주 월요일 새벽 1시 15분
    public void deleteExchangeData() {
        DeleteResultDto result = schedulerService.deleteExpireExchangeData();
        log.info("{}: {}", result.getMessage(), result.getDeleteCount());
    }

    // 한국 금리 크롤링 데이터 삭제
    @Scheduled(cron = "0 40 1 1 * *", zone = "Asia/Seoul") // 매달 1일 새벽 1시 40분
    public void deleteKoreaInterestData() {
        DeleteResultDto result = schedulerService.deleteExpireKoreaInterestData();
        log.info("{}: {}", result.getMessage(), result.getDeleteCount());
    }

    // 미국 금리 크롤링 데이터 삭제
    @Scheduled(cron = "0 50 1 1 * *", zone = "Asia/Seoul") // 매달 1일 새벽 1시 50분
    public void deleteUSInterestData() {
        DeleteResultDto result = schedulerService.deleteExpireUSInterestData();
        log.info("{}: {}", result.getMessage(), result.getDeleteCount());
    }
}
