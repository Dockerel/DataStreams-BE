package datastreams_knu.bigpicture.board.controller;

import java.util.List;
import java.util.NoSuchElementException;

import datastreams_knu.bigpicture.board.entity.Board;
import datastreams_knu.bigpicture.board.service.JpaBoardService;
import datastreams_knu.bigpicture.common.domain.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/board")
public class RestBoardApiController {

	private final JpaBoardService jpaBoardService;

	@Autowired
	public RestBoardApiController(JpaBoardService jpaBoardService) {
		this.jpaBoardService = jpaBoardService;
	}

	@GetMapping()
	public ApiResponse<List<Board>> openBoardList(){
		List<Board> list = jpaBoardService.selectBoardList();
		return ApiResponse.ok(list);
	}

	@PostMapping()
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse<Board> createBoard(@RequestBody Board board){
		Board savedBoard = jpaBoardService.saveBoard(board);
		return ApiResponse.of(HttpStatus.CREATED, "Board created successfully", savedBoard);
	}

	@GetMapping("/{boardIdx}")
	public ApiResponse<Board> openBoardDetail(@PathVariable("boardIdx") long boardIdx){
		Board board = jpaBoardService.selectBoardDetail(boardIdx);
		return ApiResponse.ok(board);
	}

	@PutMapping("/{boardIdx}")
	public ApiResponse<Board> updateBoard(@PathVariable("boardIdx") long boardIdx, @RequestBody Board boardDetails){
		if (!StringUtils.hasText(boardDetails.getBoardPassword())) {
			throw new IllegalArgumentException("Password is required.");
		}

		Board existingBoard = jpaBoardService.selectBoardDetail(boardIdx);
		if (!existingBoard.getBoardPassword().equals(boardDetails.getBoardPassword())) {
			throw new IllegalArgumentException("Incorrect password.");
		}

		existingBoard.setTitle(boardDetails.getTitle());
		if (boardDetails.getContents() != null) {
			existingBoard.setContents(boardDetails.getContents());
		}

		Board updatedBoard = jpaBoardService.saveBoard(existingBoard);
		return ApiResponse.ok(updatedBoard);
	}

	@DeleteMapping("/{boardIdx}")
	public ApiResponse<Object> deleteBoard(@PathVariable("boardIdx") long boardIdx, @RequestParam String password){
		if (!StringUtils.hasText(password)) {
			throw new IllegalArgumentException("Password is required.");
		}

		Board boardToDelete = jpaBoardService.selectBoardDetail(boardIdx);
		if (!boardToDelete.getBoardPassword().equals(password)) {
			throw new IllegalArgumentException("Incorrect password.");
		}

		jpaBoardService.deleteBoard(boardIdx);
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