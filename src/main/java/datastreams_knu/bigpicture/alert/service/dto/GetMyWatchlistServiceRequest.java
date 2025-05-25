package datastreams_knu.bigpicture.alert.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetMyWatchlistServiceRequest {
    private String uuid;

    @Builder
    private GetMyWatchlistServiceRequest(String uuid) {
        this.uuid = uuid;
    }

    public static GetMyWatchlistServiceRequest of(String uuid) {
        return GetMyWatchlistServiceRequest.builder().uuid(uuid).build();
    }
}
