package datastreams_knu.bigpicture.board.service;

import java.util.List;
import datastreams_knu.bigpicture.board.entity.Board;

public interface JpaBoardService {
    List<Board> selectBoardList();

    Board saveBoard(Board board);

    Board selectBoardDetail(long boardIdx);

    void deleteBoard(long boardIdx);

//    BoardFileEntity selectBoardFileInformation(int boardIdx, int idx);
}