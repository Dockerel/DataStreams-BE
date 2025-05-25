package datastreams_knu.bigpicture.alert.controller.dto;

import datastreams_knu.bigpicture.alert.service.dto.GetMyWatchlistServiceRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GetMyWatchlistRequest {
    private String uuid;

    public GetMyWatchlistServiceRequest toServiceRequest() {
        return GetMyWatchlistServiceRequest.of(uuid);
    }
}
