package datastreams_knu.bigpicture.interest.agent;

import datastreams_knu.bigpicture.common.config.AiModelConfig;
import datastreams_knu.bigpicture.common.util.WebClientUtil;
import datastreams_knu.bigpicture.interest.agent.dto.DateRangeDto;
import datastreams_knu.bigpicture.interest.agent.dto.InterestCrawlingResultDto;
import datastreams_knu.bigpicture.interest.agent.dto.KoreaInterestCrawlingDto;
import datastreams_knu.bigpicture.interest.agent.dto.USInterestCrawlingDto;
import datastreams_knu.bigpicture.interest.domain.KoreaInterest;
import datastreams_knu.bigpicture.interest.domain.USInterest;
import datastreams_knu.bigpicture.interest.repository.KoreaInterestRepository;
import datastreams_knu.bigpicture.interest.repository.USInterestRepository;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.model.chat.ChatLanguageModel;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static datastreams_knu.bigpicture.interest.agent.dto.KoreaInterestCrawlingDto.StatisticRow;
import static datastreams_knu.bigpicture.interest.agent.dto.USInterestCrawlingDto.ObservationsRow;

@Component
public class InterestCrawlingAgent {

    private final WebClientUtil webClientUtil;
    private final AiModelConfig aiModelConfig;
    private final KoreaInterestRepository koreaInterestRepository;
    private final USInterestRepository usInterestRepository;
    private final TransactionTemplate txTemplate;

    public InterestCrawlingAgent(AiModelConfig aiModelConfig, WebClientUtil webClientUtil, KoreaInterestRepository koreaInterestRepository, USInterestRepository usInterestRepository, PlatformTransactionManager transactionManager) {
        this.aiModelConfig = aiModelConfig;
        this.webClientUtil = webClientUtil;
        this.koreaInterestRepository = koreaInterestRepository;
        this.usInterestRepository = usInterestRepository;
        this.txTemplate = new TransactionTemplate(transactionManager);
    }

    private ChatLanguageModel model;

    @Value("${ecos.api.base-url}")
    private String ecosBaseUrl;
    @Value("${ecos.api.key}")
    private String ecosApiKey;

    @Value("${fred.api.base-url}")
    private String fredBaseUrl;
    @Value("${fred.api.key}")
    private String fredApiKey;

    @PostConstruct
    private void setup() {
        this.model = aiModelConfig.openAiChatModel();
    }

    @Tool("지난 n년 동안의 한국 금리를 크롤링합니다.")
    public KoreaInterestCrawlingDto crawlingInterestsOfKorea(int n) {
        String dateRange = getKoreaDateRange(n);
        String url = createKoreaInterestUrl(dateRange);
        return webClientUtil.get(url, KoreaInterestCrawlingDto.class);
    }

    @Tool("크롤링 된 지난 5년 동안의 한국 금리를 DB에 저장하고 처리 성공 여부를 반환합니다.")
    public InterestCrawlingResultDto saveKoreaInterest(KoreaInterestCrawlingDto koreaInterestCrawlingDto) {
        return txTemplate.execute((status) -> {
            try {
                List<StatisticRow> interestDataRows = koreaInterestCrawlingDto.getStatisticSearch().getRow();
                for (StatisticRow row : interestDataRows) {
                    LocalDate interestDate = parseStringToLocalDate(row.getTime());
                    Float interestRate = Float.parseFloat(row.getDataValue());
                    KoreaInterest koreaInterest = KoreaInterest.of(interestDate, interestRate);
                    koreaInterestRepository.save(koreaInterest);
                }
                return InterestCrawlingResultDto.of(true, "성공");
            } catch (Exception e) {
                return InterestCrawlingResultDto.of(false, "실패");
            }
        });
    }

    @Tool("지난 n년 동안의 미국 금리를 크롤링합니다.")
    public USInterestCrawlingDto crawlingInterestsOfUS(int n) {
        DateRangeDto dateRange = getUSDateRange(n);
        String url = createUSInterestUrl(dateRange);
        return webClientUtil.get(url, USInterestCrawlingDto.class);
    }

    @Tool("크롤링 된 지난 5년 동안의 미국 금리를 DB에 저장하고 처리 성공 여부를 반환합니다.")
    public InterestCrawlingResultDto saveUSInterest(USInterestCrawlingDto usInterestCrawlingDto) {
        return txTemplate.execute((status) -> {
            try {
                List<ObservationsRow> rows = usInterestCrawlingDto.getObservations();
                for (ObservationsRow row : rows) {
                    LocalDate interestDate = parseStringToLocalDate(row.getTime());
                    Float interestRate = Float.parseFloat(row.getDataValue());
                    USInterest usInterest = USInterest.of(interestDate, interestRate);
                    usInterestRepository.save(usInterest);
                }
                return InterestCrawlingResultDto.of(true, "성공");
            } catch (Exception e) {
                return InterestCrawlingResultDto.of(false, "실패");
            }
        });
    }

    private static LocalDate parseStringToLocalDate(String dateString) {
        String pattern;
        if (dateString.length() == 6) {
            pattern = "yyyyMM";
        } else {
            pattern = "yyyy-MM-dd";
        }
        YearMonth yearMonth = YearMonth.parse(dateString, DateTimeFormatter.ofPattern(pattern));
        return yearMonth.atDay(1);
    }

    private String createKoreaInterestUrl(String dateRange) {
        StringBuilder sb = new StringBuilder();
        sb.append(ecosBaseUrl)
            .append(ecosApiKey)
            .append("/json/kr/1/1000/722Y001/M/")
            .append(dateRange)
            .append("/0101000");
        return sb.toString();
    }

    private String createUSInterestUrl(DateRangeDto dateRange) {
        StringBuilder sb = new StringBuilder();
        sb.append(fredBaseUrl)
            .append("?series_id=FEDFUNDS")
            .append("&api_key=")
            .append(fredApiKey)
            .append("&file_type=json")
            .append("&frequency=m")
            .append("&observation_start=")
            .append(dateRange.getStartYear())
            .append("&observation_end=")
            .append(dateRange.getEndYear());
        return sb.toString();
    }

    private static String getKoreaDateRange(int year) {
        YearMonth yearMonth = YearMonth.from(LocalDate.now());
        String formattedYearMonthDay = formatYearMonth(yearMonth); // 오늘 기준 년월
        YearMonth yearsAgoYearMonth = yearMonth.minusYears(year);
        String formattedYearMonthFiveYearsAgo = formatYearMonth(yearsAgoYearMonth);
        return formattedYearMonthFiveYearsAgo + "/" + formattedYearMonthDay;
    }

    private static DateRangeDto getUSDateRange(int year) {
        LocalDate todayLocalDate = LocalDate.now();
        LocalDate yearsAgoLocalDate = todayLocalDate.minusYears(year);
        return DateRangeDto.builder()
            .startYear(yearsAgoLocalDate.toString())
            .endYear(todayLocalDate.toString())
            .build();
    }

    private static String formatYearMonth(YearMonth yearMonth) {
        return yearMonth.toString().replace("-", "");
    }
}