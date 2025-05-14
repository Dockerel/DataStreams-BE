package datastreams_knu.bigpicture.board.service;

import datastreams_knu.bigpicture.board.entity.Board;
import datastreams_knu.bigpicture.board.entity.Comment;
import datastreams_knu.bigpicture.board.repository.CommentRepository;
import datastreams_knu.bigpicture.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, BoardRepository boardRepository) {
        this.commentRepository = commentRepository;
        this.boardRepository = boardRepository;
    }

    @Override
    @Transactional
    public Comment createComment(Long boardIdx, Comment comment){
        Board board = boardRepository.findById(boardIdx)
                .orElseThrow(() -> new NoSuchElementException("Board not found with ID: " + boardIdx));

        if (!StringUtils.hasText(comment.getComment())) {
            throw new IllegalArgumentException("Comment cannot be empty.");
        }
        if (!StringUtils.hasText(comment.getCommentPassword())) {
            throw new IllegalArgumentException("password is required.");
        }

        comment.setBoard(board);
        comment.setCreatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getCommentsByBoardIdx(Long boardIdx){
        if (!boardRepository.existsById(boardIdx)) {
            throw new NoSuchElementException("Board not found with ID: " + boardIdx);
        }
        return commentRepository.findByBoardBoardIdxOrderByCommentIdxAsc(boardIdx);
    }

    @Override
    @Transactional(readOnly = true)
    public Comment getCommentById(Long commentIdx){
        return commentRepository.findById(commentIdx)
                .orElseThrow(() -> new NoSuchElementException("Comment not found with ID: " + commentIdx));
    }

    @Override
    @Transactional
    public Comment updateComment(Long commentIdx, Comment commentDetails){
        Comment existingComment = getCommentById(commentIdx);

        if (!StringUtils.hasText(commentDetails.getCommentPassword())) {
            throw new IllegalArgumentException("Password is required for comment update.");
        }
        if (!existingComment.getCommentPassword().equals(commentDetails.getCommentPassword())) {
            throw new IllegalArgumentException("Incorrect password for comment update.");
        }

        if (StringUtils.hasText(commentDetails.getComment())) {
            existingComment.setComment(commentDetails.getComment());
        }
        return existingComment;
    }

    @Override
    @Transactional
    public void deleteComment(Long commentIdx, String password){
        Comment commentToDelete = getCommentById(commentIdx);

        if (!StringUtils.hasText(password)) {
            throw new IllegalArgumentException("Password is required for comment deletion.");
        }
        if (!commentToDelete.getCommentPassword().equals(password)) {
            throw new IllegalArgumentException("Incorrect password for comment deletion.");
        }

        commentRepository.delete(commentToDelete);
    }
}