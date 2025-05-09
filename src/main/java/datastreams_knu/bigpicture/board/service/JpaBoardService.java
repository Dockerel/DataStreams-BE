package datastreams_knu.bigpicture.board.service;

import java.util.List;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import datastreams_knu.bigpicture.board.entity.BoardEntity;
import datastreams_knu.bigpicture.board.entity.BoardFileEntity;

public interface JpaBoardService {
    List<BoardEntity> selectBoardList() throws Exception;
    void saveBoard(BoardEntity board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception;
    BoardEntity selectBoardDetail(int boardIdx) throws Exception;
    void deleteBoard(int boardIdx) throws Exception;
    BoardFileEntity selectBoardFileInformation(int boardIdx, int idx) throws Exception;
}