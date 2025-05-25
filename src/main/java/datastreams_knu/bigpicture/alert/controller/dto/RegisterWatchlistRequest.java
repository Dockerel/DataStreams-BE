package datastreams_knu.bigpicture.alert.controller.dto;

import datastreams_knu.bigpicture.alert.service.dto.RegisterWatchlistServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RegisterWatchlistRequest {
    private String uuid;
    private String stockName;
    private String stockType;

    @Builder
    public RegisterWatchlistRequest(String uuid, String stockName, String stockType) {
        this.uuid = uuid;
        this.stockName = stockName;
        this.stockType = stockType;
    }

    public RegisterWatchlistServiceRequest toServiceRequest() {
        return RegisterWatchlistServiceRequest.of(uuid, stockName, stockType);
    }
}
