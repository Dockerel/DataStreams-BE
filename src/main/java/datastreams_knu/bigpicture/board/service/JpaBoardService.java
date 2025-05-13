package datastreams_knu.bigpicture.board.service;

import java.util.List;

import datastreams_knu.bigpicture.board.dto.BoardUpdateRequestDto;
import datastreams_knu.bigpicture.board.entity.Board;

public interface JpaBoardService {
    List<Board> selectBoardList();

    Board saveBoard(Board board);

    Board selectBoardDetail(long boardIdx);

    Board updateBoard(long boardIdx, BoardUpdateRequestDto requestDto);

    void deleteBoard(long boardIdx, String password);

//    BoardFileEntity selectBoardFileInformation(int boardIdx, int idx);
}