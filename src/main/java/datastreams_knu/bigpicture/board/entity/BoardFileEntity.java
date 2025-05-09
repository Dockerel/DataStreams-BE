package datastreams_knu.bigpicture.board.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="t_jpa_file")
@NoArgsConstructor
@Data
public class BoardFileEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int idx;

	@Column(nullable=false)
	private String originalFileName;

	@Column(nullable=false)
	private String storedFilePath;

	@Column(nullable=false)
	private long fileSize;

	@Column(nullable=false)
	private String creatorId;

	@Column(nullable=false)
	private LocalDateTime createdDatetime = LocalDateTime.now();

	private String updaterId;

	private LocalDateTime updatedDatetime;
}