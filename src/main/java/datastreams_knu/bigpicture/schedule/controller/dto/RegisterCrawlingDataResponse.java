package datastreams_knu.bigpicture.schedule.controller.dto;

import datastreams_knu.bigpicture.schedule.entity.CrawlingInfo;
import datastreams_knu.bigpicture.schedule.entity.CrawlingSeed;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RegisterCrawlingDataResponse {

    private String stockType;
    private String stockName;
    private String stockKeyword;

    @Builder
    public RegisterCrawlingDataResponse(String stockType, String stockName, String stockKeyword) {
        this.stockType = stockType;
        this.stockName = stockName;
        this.stockKeyword = stockKeyword;
    }

    public static RegisterCrawlingDataResponse from(CrawlingInfo crawlingInfo) {
        return RegisterCrawlingDataResponse.builder()
                .stockType(crawlingInfo.getStockType())
                .stockName(crawlingInfo.getStockName())
                .stockKeyword(crawlingInfo.getStockKeyword())
                .build();
    }

    public static RegisterCrawlingDataResponse from(CrawlingSeed crawlingSeed) {
        return RegisterCrawlingDataResponse.builder()
                .stockType(crawlingSeed.getStockType())
                .stockName(crawlingSeed.getStockName())
                .stockKeyword(crawlingSeed.getStockKeyword())
                .build();
    }
}
