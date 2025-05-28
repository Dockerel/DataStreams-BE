package datastreams_knu.bigpicture.schedule.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RegisterCrawlingDataServiceRequest {

    private String stockType;
    private String stockName;

    @Builder
    public RegisterCrawlingDataServiceRequest(String stockType, String stockName) {
        this.stockType = stockType;
        this.stockName = stockName;
    }

    public static RegisterCrawlingDataServiceRequest of(String stockType, String stockName) {
        return RegisterCrawlingDataServiceRequest.builder()
                .stockType(stockType)
                .stockName(stockName)
                .build();
    }
}
