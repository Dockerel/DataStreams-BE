package datastreams_knu.bigpicture.news.service;

import datastreams_knu.bigpicture.common.dto.CrawlingResultDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

@SpringBootTest
class NewsCrawlingServiceTest {

    @Autowired
    NewsCrawlingService newsCrawlingService;

    @Test
    @Commit
    void test() {
        CrawlingResultDto crawling1 = newsCrawlingService.crawling("keyword", "애플");
        System.out.println(crawling1.getMessage());
//        CrawlingResultDto crawling2 = newsCrawlingService.crawling("keyword", "MSFT");
//        System.out.println(crawling2.getMessage());
//        CrawlingResultDto crawling3 = newsCrawlingService.crawling("keyword", "삼성전자");
//        System.out.println(crawling3.getMessage());
    }

}