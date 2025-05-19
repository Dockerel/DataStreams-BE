package datastreams_knu.bigpicture.board.controller;

import datastreams_knu.bigpicture.board.dto.CommentCreateRequestDto;
import datastreams_knu.bigpicture.board.dto.CommentPasswordDto;
import datastreams_knu.bigpicture.board.entity.Comment;
import datastreams_knu.bigpicture.board.service.CommentService;
import datastreams_knu.bigpicture.common.domain.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/comments")
public class RestCommentApiController {

    private final CommentService commentService;

    public RestCommentApiController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Comment> createComment(@RequestBody CommentCreateRequestDto requestDto) {
        Comment commentToCreate = new Comment();
        commentToCreate.setComment(requestDto.getComment());
        commentToCreate.setCommentPassword(requestDto.getCommentPassword());
        Comment createdComment = commentService.createComment(requestDto.getBoardIdx(), commentToCreate);
        return ApiResponse.of(HttpStatus.CREATED, "Comment created successfully", createdComment);
    }

    @GetMapping()
    public ApiResponse<List<Comment>> getCommentsByBoard(@RequestParam Long boardIdx) {
        List<Comment> comments = commentService.getCommentsByBoardIdx(boardIdx);
        return ApiResponse.ok(comments);
    }

    @PutMapping("/{commentIdx}")
    public ApiResponse<Comment> updateComment(@PathVariable Long commentIdx,
                                              @RequestBody Comment commentDetails) {
        Comment updatedComment = commentService.updateComment(commentIdx, commentDetails);
        return ApiResponse.ok(updatedComment);
    }

    @DeleteMapping("/{commentIdx}")
    public ApiResponse<Object> deleteComment(@PathVariable Long commentIdx, @RequestBody CommentPasswordDto commentPassword) {
        String password = commentPassword.getCommentPassword();
        commentService.deleteComment(commentIdx, password);
        return ApiResponse.of(HttpStatus.OK, "Comment deleted successfully (ID: " + commentIdx + ")");
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Object> handleNoSuchElementException(NoSuchElementException ex) {
        return ApiResponse.of(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ApiResponse.of(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Object> handleAllOtherExceptions(Exception ex) {
        return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "An internal server error occurred while processing the request.");
    }
}