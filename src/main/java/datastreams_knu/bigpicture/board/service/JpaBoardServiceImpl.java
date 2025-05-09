package datastreams_knu.bigpicture.board.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import datastreams_knu.bigpicture.board.entity.BoardEntity;
import datastreams_knu.bigpicture.board.entity.BoardFileEntity;
import datastreams_knu.bigpicture.board.repository.JpaBoardRepository;
import datastreams_knu.bigpicture.common.util.FileUtils;

@Service
public class JpaBoardServiceImpl implements JpaBoardService {

    @Autowired
    private JpaBoardRepository jpaBoardRepository;

    @Autowired
    private FileUtils fileUtils;

    @Override
    public List<BoardEntity> selectBoardList() throws Exception {
        return jpaBoardRepository.findAllByOrderByBoardIdxDesc();
    }

    @Override
    @Transactional
    public void saveBoard(BoardEntity board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception {
        if (board.getBoardIdx() == 0) { // New entity
            if (board.getCreatorId() == null || board.getCreatorId().isEmpty()) {
                board.setCreatorId("default_creator");
            }
            board.setCreatedDatetime(LocalDateTime.now());
        } else { // Existing entity (update)
            if (board.getUpdaterId() == null || board.getUpdaterId().isEmpty()) {
                board.setUpdaterId("default_updater");
            }
            board.setUpdatedDatetime(LocalDateTime.now());
        }

        if (multipartHttpServletRequest != null) {
            List<BoardFileEntity> newFiles = fileUtils.parseFileInfo(multipartHttpServletRequest);
            if(!CollectionUtils.isEmpty(newFiles)){
                if (board.getFileList() != null) {
                    board.getFileList().clear();
                    board.getFileList().addAll(newFiles);
                } else {
                    board.setFileList(newFiles);
                }

                for (BoardFileEntity file : newFiles) {
                    if (file.getCreatorId() == null || file.getCreatorId().isEmpty()) {
                        file.setCreatorId(board.getCreatorId());
                    }
                    file.setCreatedDatetime(LocalDateTime.now());
                }
            } else if (board.getBoardIdx() != 0 && multipartHttpServletRequest.getFileNames().hasNext() && multipartHttpServletRequest.getFile(multipartHttpServletRequest.getFileNames().next()).isEmpty()) {
                // If it's an update, a multipart request was made, but it contained no actual files (or empty file parts), treat as clearing files.
                if (board.getFileList() != null) {
                    board.getFileList().clear();
                }
            }
        }
        jpaBoardRepository.save(board);
    }

    @Override
    @Transactional
    public BoardEntity selectBoardDetail(int boardIdx) throws Exception{
        Optional<BoardEntity> optional = jpaBoardRepository.findById(boardIdx);
        if(optional.isPresent()){
            BoardEntity board = optional.get();
            board.setHitCnt(board.getHitCnt() + 1);
            jpaBoardRepository.save(board);
            return board;
        }
        else {
            return null;
        }
    }

    @Override
    @Transactional
    public void deleteBoard(int boardIdx) throws Exception {
        if (!jpaBoardRepository.existsById(boardIdx)) {
            throw new Exception("Board not found with id: " + boardIdx); // Or a custom exception
        }
        jpaBoardRepository.deleteById(boardIdx);
    }

    @Override
    public BoardFileEntity selectBoardFileInformation(int boardIdx, int idx) throws Exception {
        BoardFileEntity file = jpaBoardRepository.findBoardFile(boardIdx, idx);
        if (file == null) {
            // Optionally throw an exception if file not found is an error state
            // throw new Exception("File not found with id: " + idx + " for board: " + boardIdx);
        }
        return file;
    }
}