package datastreams_knu.bigpicture.report.facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import datastreams_knu.bigpicture.report.service.ReportService;
import datastreams_knu.bigpicture.report.service.dto.CreateReportResponse;
import datastreams_knu.bigpicture.report.service.dto.CreateReportServiceRequest;
import datastreams_knu.bigpicture.report.util.LockProvider;
import dev.langchain4j.exception.InternalServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReportFacade {

    private static final String CACHE_KEY = "report";
    private static final String LOCK_KEY = "lock-report";

    public static final int RETRY_COUNT = 1000;
    public static final int LOCK_TIMEOUT_MS = 60000;

    private final ReportService reportService;

    private final LockProvider lockProvider;

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public CreateReportResponse getReport(CreateReportServiceRequest request) {
        String reportType = request.getReportType();
        String stockName = request.getStockName();

        StringBuilder cacheKeyBuilder = new StringBuilder(CACHE_KEY).append(":").append(reportType);
        if ("stock".equals(reportType)) {
            cacheKeyBuilder.append(":").append(stockName);
        }
        String cacheKey = cacheKeyBuilder.toString();

        StringBuilder lockKeyBuilder = new StringBuilder(LOCK_KEY).append(":").append(reportType);
        if ("stock".equals(reportType)) {
            lockKeyBuilder.append(":").append(stockName);
        }
        String lockKey = lockKeyBuilder.toString();


        // 1. 캐시 확인
        String cachedData = redisTemplate.opsForValue().get(cacheKey);
        if (cachedData != null) {
            try {
                return objectMapper.readValue(cachedData, CreateReportResponse.class);
            } catch (JsonProcessingException e) {
                log.error("CachedData parsing error : {}", cacheKey, e);
            }
        }

        // 2. 분산 락 획득 시도
        for (int retry = 0; retry < RETRY_COUNT; retry++) {
            boolean lockAcquired = lockProvider.tryLock(lockKey, LOCK_TIMEOUT_MS);
            // 3. 랜덤 지터만큼 대기
            if (!lockAcquired) {
                long jitter = ThreadLocalRandom.current().nextLong(200, 300);
                try {
                    Thread.sleep(jitter);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                continue;
            }

            try {
                // 4. Double-Checked Locking
                cachedData = redisTemplate.opsForValue().get(cacheKey);
                if (cachedData != null) {
                    try {
                        return objectMapper.readValue(cachedData, CreateReportResponse.class);
                    } catch (JsonProcessingException e) {
                        log.error("CachedData parsing error : {}", cacheKey, e);
                    }
                }

                // 5. 데이터 처리
                CreateReportResponse report = reportService.createReport(request);

                // 6. 캐시 저장
                try {
                    redisTemplate.opsForValue().set(cacheKey, objectMapper.writeValueAsString(report), 24, TimeUnit.HOURS);
                } catch (JsonProcessingException e) {
                    log.error("CalculatedSentimentScore serializing error : {}", cacheKey, e);
                }

                return report;
            } finally {
                // 7. 락 반환
                lockProvider.releaseLock(LOCK_KEY);
            }
        }
        throw new InternalServerException("잠시 후 다시 리포트 생성을 시도해주세요.");
    }
}
