package datastreams_knu.bigpicture.board.controller;

import datastreams_knu.bigpicture.board.dto.BoardPasswordDto;
import datastreams_knu.bigpicture.board.dto.BoardUpdateRequestDto;
import datastreams_knu.bigpicture.board.entity.Board;
import datastreams_knu.bigpicture.board.service.JpaBoardService;
import datastreams_knu.bigpicture.common.domain.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/board")
public class RestBoardApiController {

    private final JpaBoardService jpaBoardService;

    @Autowired
    public RestBoardApiController(JpaBoardService jpaBoardService) {
        this.jpaBoardService = jpaBoardService;
    }

    @GetMapping()
    public ApiResponse<List<Board>> openBoardList() {
        List<Board> list = jpaBoardService.selectBoardList();
        return ApiResponse.ok(list);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Board> createBoard(@RequestBody Board board) {
        Board savedBoard = jpaBoardService.saveBoard(board);
        return ApiResponse.of(HttpStatus.CREATED, "Board created successfully", savedBoard);
    }

    @GetMapping("/{boardIdx}")
    public ApiResponse<Board> openBoardDetail(@PathVariable("boardIdx") long boardIdx) {
        Board board = jpaBoardService.selectBoardDetail(boardIdx);
        return ApiResponse.ok(board);
    }

    @PutMapping("/{boardIdx}")
    public ApiResponse<Board> updateBoard(@PathVariable("boardIdx") long boardIdx,
                                          @RequestBody BoardUpdateRequestDto requestDto) {
        Board updatedBoard = jpaBoardService.updateBoard(boardIdx, requestDto);
        return ApiResponse.ok(updatedBoard);
    }

    @DeleteMapping("/{boardIdx}")
    public ApiResponse<Object> deleteBoard(@PathVariable("boardIdx") long boardIdx, @RequestBody BoardPasswordDto passwordDto) {
        String password = passwordDto.getBoardPassword();
        jpaBoardService.deleteBoard(boardIdx, password);
        return ApiResponse.of(HttpStatus.OK, "Board deleted successfully (ID: " + boardIdx + ")");
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