package datastreams_knu.bigpicture.board.repository;

import datastreams_knu.bigpicture.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByBoardBoardIdxOrderByIdxAsc(Long boardIdx);
}