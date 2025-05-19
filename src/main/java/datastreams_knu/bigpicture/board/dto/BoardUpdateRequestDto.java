package datastreams_knu.bigpicture.board.dto;

import lombok.*;

@Getter
@NoArgsConstructor
public class BoardUpdateRequestDto {
    private String title;
    private String contents;
    private String boardPassword;
}