package datastreams_knu.bigpicture.board.controller;

import java.util.List;
import datastreams_knu.bigpicture.board.entity.BoardEntity;
import datastreams_knu.bigpicture.board.service.JpaBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
@RequestMapping("/api/board")
public class RestBoardApiController {

	@Autowired
	private JpaBoardService jpaBoardService;

	@GetMapping("")
	public ResponseEntity<List<BoardEntity>> openBoardList() throws Exception{
		List<BoardEntity> list = jpaBoardService.selectBoardList();
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@PostMapping("/write")
	public ResponseEntity<BoardEntity> insertBoard(@RequestBody BoardEntity board, MultipartHttpServletRequest request) throws Exception{
		jpaBoardService.saveBoard(board, request);
		return new ResponseEntity<>(board, HttpStatus.CREATED);
	}

	@GetMapping("/{boardIdx}")
	public ResponseEntity<BoardEntity> openBoardDetail(@PathVariable("boardIdx") int boardIdx) throws Exception{
		BoardEntity board = jpaBoardService.selectBoardDetail(boardIdx);
		if (board != null) {
			return new ResponseEntity<>(board, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/{boardIdx}")
	public ResponseEntity<BoardEntity> updateBoard(@PathVariable("boardIdx") int boardIdx, @RequestBody BoardEntity boardDetails, MultipartHttpServletRequest request) throws Exception{
		BoardEntity existingBoard = jpaBoardService.selectBoardDetail(boardIdx);
		if (existingBoard == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		existingBoard.setTitle(boardDetails.getTitle());
		existingBoard.setContents(boardDetails.getContents());

		jpaBoardService.saveBoard(existingBoard, request);
		return new ResponseEntity<>(existingBoard, HttpStatus.OK);
	}

	@DeleteMapping("/{boardIdx}")
	public ResponseEntity<Void> deleteBoard(@PathVariable("boardIdx") int boardIdx) throws Exception{
		jpaBoardService.deleteBoard(boardIdx);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}