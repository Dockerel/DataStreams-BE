package datastreams_knu.bigpicture.board.repository;

import datastreams_knu.bigpicture.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findAllByOrderByBoardIdxDesc();

//	@Query(value = "SELECT * FROM t_jpa_file WHERE board_idx = :boardIdx AND idx = :idx", nativeQuery = true)
//	BoardFileEntity findBoardFile(@Param("boardIdx") long boardIdx, @Param("idx") long idx);
}