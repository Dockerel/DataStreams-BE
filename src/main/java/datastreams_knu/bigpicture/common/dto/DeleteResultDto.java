package datastreams_knu.bigpicture.common.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DeleteResultDto {

    private int deleteCount;
    private String message;

    @Builder
    public DeleteResultDto(int deleteCount, String message) {
        this.deleteCount = deleteCount;
        this.message = message;
    }

    public static DeleteResultDto of(int deleteCount, String message) {
        return DeleteResultDto.builder()
            .deleteCount(deleteCount)
            .message(message)
            .build();
    }
}
