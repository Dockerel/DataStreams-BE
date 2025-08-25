package datastreams_knu.bigpicture.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StockKeywordResolverTest {

    @Autowired
    private StockKeywordResolver stockKeywordResolver;
    @Autowired
    TickerParser tickerParser;

    @DisplayName("")
    @Test
    void test() {
        String result = tickerParser.parseTicker("SMCI");
        System.out.println("result = " + result);
    }
}