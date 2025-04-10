package datastreams_knu.bigpicture.schedule.controller.dto;

import datastreams_knu.bigpicture.schedule.entity.CrawlingInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RegisterCrawlingDataResponse {

    private String stockType;
    private String stockName;
    private String newsKeyword;

    @Builder
    public RegisterCrawlingDataResponse(String stockType, String stockName, String newsKeyword) {
        this.stockType = stockType;
        this.stockName = stockName;
        this.newsKeyword = newsKeyword;
    }

    public static RegisterCrawlingDataResponse of(CrawlingInfo crawlingInfo) {
        return RegisterCrawlingDataResponse.builder()
            .stockType(crawlingInfo.getStockType())
            .stockName(crawlingInfo.getStockName())
            .newsKeyword(crawlingInfo.getNewsKeyword())
            .build();
    }
}
