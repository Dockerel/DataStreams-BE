package datastreams_knu.bigpicture.board.service;

import datastreams_knu.bigpicture.board.entity.Comment;

import java.util.List;

public interface CommentService {
    Comment createComment(Long boardIdx, Comment comment);

    List<Comment> getCommentsByBoardIdx(Long boardIdx);

    Comment getCommentById(Long commentIdx);

    Comment updateComment(Long boardIdx, Long commentIdx, Comment commentDetails);

    void deleteComment(Long boardIdx, Long commentIdx, String password);
}