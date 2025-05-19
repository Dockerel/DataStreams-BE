package datastreams_knu.bigpicture.alert.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteWatchlistServiceRequest {
    private String uuid;
    private String stockName;

    @Builder
    public DeleteWatchlistServiceRequest(String uuid, String stockName) {
        this.uuid = uuid;
        this.stockName = stockName;
    }

    public static DeleteWatchlistServiceRequest of(String uuid, String stockName) {
        return DeleteWatchlistServiceRequest.builder()
                .uuid(uuid)
                .stockName(stockName)
                .build();
    }
}
