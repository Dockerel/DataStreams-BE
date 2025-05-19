package datastreams_knu.bigpicture.alert.controller.dto;

import datastreams_knu.bigpicture.alert.service.dto.DeleteWatchlistServiceRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteWatchlistRequest {
    private String uuid;
    private String stockName;

    public DeleteWatchlistServiceRequest toServiceRequest() {
        return DeleteWatchlistServiceRequest.of(uuid, stockName);
    }
}
