package datastreams_knu.bigpicture.board.service;

import datastreams_knu.bigpicture.board.dto.BoardUpdateRequestDto;
import datastreams_knu.bigpicture.board.entity.Board;
import datastreams_knu.bigpicture.board.repository.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class JpaBoardServiceImpl implements JpaBoardService {

    private final BoardRepository boardRepository;

    public JpaBoardServiceImpl(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Board> selectBoardList() {
        return boardRepository.findAllByOrderByBoardIdxDesc();
    }

    @Override
    @Transactional
    public Board saveBoard(Board board) {
        if (board.getBoardIdx() == 0) {
            if (!StringUtils.hasText(board.getBoardPassword())) {
                throw new IllegalArgumentException("Password is required.");
            }
            board.setCreatedAt(LocalDateTime.now());
        } else {
            board.setUpdatedAt(LocalDateTime.now());
        }
        return boardRepository.save(board);
    }

    @Override
    @Transactional
    public Board selectBoardDetail(long boardIdx) {
        Board board = boardRepository.findById(boardIdx)
                .orElseThrow(() -> new IllegalArgumentException("Board not found with id: " + boardIdx + " for deletion."));

        board.setViewCount(board.getViewCount() + 1);
        return board;
    }

    @Override
    @Transactional
    public Board updateBoard(long boardIdx, BoardUpdateRequestDto requestDto) {
        Board existingBoard = boardRepository.findById(boardIdx)
                .orElseThrow(() -> new NoSuchElementException("Board not found with ID: " + boardIdx));

        if (!StringUtils.hasText(requestDto.getBoardPassword())) {
            throw new IllegalArgumentException("Password is required for update.");
        }
        if (!existingBoard.getBoardPassword().equals(requestDto.getBoardPassword())) {
            throw new IllegalArgumentException("Incorrect password.");
        }

        if (StringUtils.hasText(requestDto.getTitle())) {
            existingBoard.setTitle(requestDto.getTitle());
        }
        if (StringUtils.hasText(requestDto.getContents())) {
            existingBoard.setContents(requestDto.getContents());
        }

        existingBoard.setUpdatedAt(LocalDateTime.now());
        return existingBoard;
    }

    @Override
    @Transactional
    public void deleteBoard(long boardIdx, String password) {
        Board boardToDelete = boardRepository.findById(boardIdx)
                .orElseThrow(() -> new NoSuchElementException("Board not found with id: " + boardIdx + " for deletion."));

        if (!StringUtils.hasText(password)) {
            throw new IllegalArgumentException("Password is required for deletion.");
        }
        if (!boardToDelete.getBoardPassword().equals(password)) {
            throw new IllegalArgumentException("Incorrect password for deletion.");
        }
        boardRepository.delete(boardToDelete);
    }


//    @Override
//    @Transactional
//    public void saveBoard(BoardEntity board, MultipartHttpServletRequest multipartHttpServletRequest){
//        if (board.getBoardIdx() == 0) { // New
//            if (board.getCreatorId() == null || board.getCreatorId().isEmpty()) {
//                board.setCreatorId("default_creator");
//            }
//            board.setCreatedDatetime(LocalDateTime.now());
//        } else { // Existing (update)
//            if (board.getUpdaterId() == null || board.getUpdaterId().isEmpty()) {
//                board.setUpdaterId("default_updater");
//            }
//            board.setUpdatedDatetime(LocalDateTime.now());
//        }
//
//        if (multipartHttpServletRequest != null) {
//            List<BoardFileEntity> newFiles = fileUtils.parseFileInfo(multipartHttpServletRequest);
//            if(!CollectionUtils.isEmpty(newFiles)){
//                if (board.getFileList() != null) {
//                    board.getFileList().clear();
//                    board.getFileList().addAll(newFiles);
//                } else {
//                    board.setFileList(newFiles);
//                }
//
//                for (BoardFileEntity file : newFiles) {
//                    if (file.getCreatorId() == null || file.getCreatorId().isEmpty()) {
//                        file.setCreatorId(board.getCreatorId());
//                    }
//                    file.setCreatedDatetime(LocalDateTime.now());
//                }
//            } else if (board.getBoardIdx() != 0 && multipartHttpServletRequest.getFileNames().hasNext() && multipartHttpServletRequest.getFile(multipartHttpServletRequest.getFileNames().next()).isEmpty()) {
//                // If it's an update, a multipart request was made, but it contained no actual files (or empty file parts), treat as clearing files.
//                if (board.getFileList() != null) {
//                    board.getFileList().clear();
//                }
//            }
//        }
//        jpaBoardRepository.save(board);
//    }

//    @Override
//    public BoardFileEntity selectBoardFileInformation(long boardIdx, long idx){
//        BoardFileEntity file = jpaBoardRepository.findBoardFile(boardIdx, idx);
//        if (file == null) {
//            // Optionally throw an exception if file not found is an error state
//            // throw new Exception("File not found with id: " + idx + " for board: " + boardIdx);
//        }
//        return file;
//    }
}