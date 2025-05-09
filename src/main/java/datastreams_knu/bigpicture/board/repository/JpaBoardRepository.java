package datastreams_knu.bigpicture.board.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import datastreams_knu.bigpicture.board.entity.BoardEntity;
import datastreams_knu.bigpicture.board.entity.BoardFileEntity;

public interface JpaBoardRepository extends CrudRepository<BoardEntity, Integer>{

	List<BoardEntity> findAllByOrderByBoardIdxDesc();

	@Query(value = "SELECT * FROM t_jpa_file WHERE board_idx = :boardIdx AND idx = :idx", nativeQuery = true)
	BoardFileEntity findBoardFile(@Param("boardIdx") int boardIdx, @Param("idx") int idx);
}