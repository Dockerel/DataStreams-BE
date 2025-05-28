package datastreams_knu.bigpicture.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentCreateRequestDto {
    private Long boardIdx;
    private String comment;
    private String commentPassword;
}