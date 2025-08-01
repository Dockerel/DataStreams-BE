package datastreams_knu.bigpicture.schedule.util;

import datastreams_knu.bigpicture.common.dto.CrawlingResultDto;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
public class RetryExecutor {

    private final static int RETRY_COUNT = 3;

    public static void execute(Supplier<CrawlingResultDto> supplier, String taskName) {
        String message = "";
        for (int i = 0; i < RETRY_COUNT; i++) {
            try {
                CrawlingResultDto result = supplier.get();
                message = result.getMessage();
                if (result.getResult()) {
                    log.info("{}: {}", taskName, message);
                    return;
                }
            } catch (Exception e) {
                log.warn("{}: {}번째 시도 중 예외 발생 - {}", taskName, i + 1, message);
            }
        }
    }
}
