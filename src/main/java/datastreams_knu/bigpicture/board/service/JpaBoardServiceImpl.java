package datastreams_knu.bigpicture.board.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import datastreams_knu.bigpicture.board.entity.Board;
import datastreams_knu.bigpicture.board.repository.JpaBoardRepository;
import org.springframework.util.StringUtils;

@Service
public class JpaBoardServiceImpl implements JpaBoardService {

    private final JpaBoardRepository jpaBoardRepository;

    public JpaBoardServiceImpl(JpaBoardRepository jpaBoardRepository) {
        this.jpaBoardRepository = jpaBoardRepository;
    }

    @Override
    public List<Board> selectBoardList(){
        return jpaBoardRepository.findAllByOrderByBoardIdxDesc();
    }

    @Override
    @Transactional
    public Board saveBoard(Board board){
        if (board.getBoardIdx() == 0) {
            if (!StringUtils.hasText(board.getBoardPassword())) {
                throw new IllegalArgumentException("Password is required.");
            }
            board.setCreatedAt(LocalDateTime.now());
        } else {
            board.setUpdatedAt(LocalDateTime.now());
        }
        return jpaBoardRepository.save(board);
    }

    @Override
    @Transactional
    public Board selectBoardDetail(long boardIdx){
        Optional<Board> optional = jpaBoardRepository.findById(boardIdx);
        if(optional.isEmpty()) {
            throw new IllegalArgumentException("Board not found with id: " + boardIdx + " for deletion.");
        }
        Board board = optional.get();
        board.setViewCount(board.getViewCount() + 1);
        return board;
    }

    @Override
    @Transactional
    public void deleteBoard(long boardIdx){
        if (!jpaBoardRepository.existsById(boardIdx)) {
            throw new NoSuchElementException("Board not found with id: " + boardIdx + " for deletion.");
        }
        jpaBoardRepository.deleteById(boardIdx);
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