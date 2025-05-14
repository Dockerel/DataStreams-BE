package datastreams_knu.bigpicture.board.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import datastreams_knu.bigpicture.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="boards")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Board extends BaseEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long boardIdx;

	@Column(nullable=false)
	private String title;

	@Column(nullable=false)
	private String contents;

	@Column(nullable=false)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String boardPassword;

	@Column(nullable=false)
	private int viewCount = 0;

	@OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@OrderBy("commentIdx asc")
	@JsonManagedReference
	private List<Comment> comments = new ArrayList<>();

//	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval = true)
//	@JoinColumn(name="board_idx")
//	private Collection<BoardFileEntity> fileList;
}