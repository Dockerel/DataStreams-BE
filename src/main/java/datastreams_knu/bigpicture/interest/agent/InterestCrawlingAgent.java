package datastreams_knu.bigpicture.interest.agent;

import datastreams_knu.bigpicture.common.config.AiModelConfig;
import datastreams_knu.bigpicture.common.util.WebClientUtil;
import datastreams_knu.bigpicture.interest.agent.dto.InterestCrawlingResultDto;
import datastreams_knu.bigpicture.interest.agent.dto.KoreaInterestCrawlingDto;
import datastreams_knu.bigpicture.interest.domain.KoreaInterest;
import datastreams_knu.bigpicture.interest.repository.InterestRepository;
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

@Component
public class InterestCrawlingAgent {

    private final WebClientUtil webClientUtil;
    private final AiModelConfig aiModelConfig;
    private final InterestRepository interestRepository;
    private final TransactionTemplate txTemplate;

    public InterestCrawlingAgent(InterestRepository interestRepository, AiModelConfig aiModelConfig, WebClientUtil webClientUtil, PlatformTransactionManager transactionManager) {
        this.interestRepository = interestRepository;
        this.aiModelConfig = aiModelConfig;
        this.webClientUtil = webClientUtil;
        this.txTemplate = new TransactionTemplate(transactionManager);
    }

    private ChatLanguageModel model;

    @Value("${ecos.api.url}")
    private String ecosBaseUrl;
    @Value("${ecos.api.key}")
    private String ecosApiKey;

    @PostConstruct
    private void setup() {
        this.model = aiModelConfig.openAiChatModel();
    }

    @Tool("지난 n년 동안의 한국 금리를 크롤링합니다.")
    public KoreaInterestCrawlingDto crawlingInterestsOfKorea(int n) {
        String dateRange = getDateRange(n);
        String url = createUrl(dateRange);
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
                    interestRepository.save(koreaInterest);
                }
                return InterestCrawlingResultDto.of(true, "성공");
            } catch (Exception e) {
                return InterestCrawlingResultDto.of(false, "실패");
            }
        });
    }

    private static LocalDate parseStringToLocalDate(String dateString) {
        YearMonth yearMonth = YearMonth.parse(dateString, DateTimeFormatter.ofPattern("yyyyMM"));
        return yearMonth.atDay(1);
    }

    private String createUrl(String dateRange) {
        StringBuilder sb = new StringBuilder();
        sb.append(ecosBaseUrl);
        sb.append(ecosApiKey);
        sb.append("/json/kr/1/1000/722Y001/M/");
        sb.append(dateRange);
        sb.append("/0101000");
        return sb.toString();
    }

    private static String getDateRange(int year) {
        YearMonth yearMonth = YearMonth.from(LocalDate.now());
        String formattedYearMonthDay = formatYearMonth(yearMonth); // 오늘 기준 년월
        YearMonth fiveYearsAgoYearMonth = yearMonth.minusYears(year);
        String formattedYearMonthFiveYearsAgo = formatYearMonth(fiveYearsAgoYearMonth);
        return formattedYearMonthFiveYearsAgo + "/" + formattedYearMonthDay;
    }

    private static String formatYearMonth(YearMonth yearMonth) {
        return yearMonth.toString().replace("-", "");
    }
}