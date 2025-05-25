package datastreams_knu.bigpicture.alert.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RegisterWatchlistServiceRequest {
    private String uuid;
    private String stockName;
    private String stockType;

    @Builder
    public RegisterWatchlistServiceRequest(String uuid, String stockName, String stockType) {
        this.uuid = uuid;
        this.stockName = stockName;
        this.stockType = stockType;
    }

    public static RegisterWatchlistServiceRequest of(String uuid, String stockName, String stockType) {
        return RegisterWatchlistServiceRequest.builder()
                .uuid(uuid)
                .stockName(stockName)
                .stockType(stockType)
                .build();
    }
}
