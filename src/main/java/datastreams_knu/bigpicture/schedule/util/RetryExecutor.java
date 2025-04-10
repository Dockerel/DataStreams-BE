package datastreams_knu.bigpicture.schedule.util;

import datastreams_knu.bigpicture.common.dto.CrawlingResultDto;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
public class RetryExecutor {

    private static int RETRY_COUNT = 3;

    public static void execute(Supplier<CrawlingResultDto> supplier, String taskName) {
        try {
            String message = "";
            for (int i = 0; i < RETRY_COUNT; i++) {
                CrawlingResultDto result = supplier.get();
                message = result.getMessage();
                if (result.getResult()) {
                    log.info("{}: {}", taskName, message);
                    return;
                }
            }
            log.error("{}: {}", taskName, message);
        } catch (Exception e) {
            log.error("{}: {}", taskName, e.getMessage());
        }
    }
}
