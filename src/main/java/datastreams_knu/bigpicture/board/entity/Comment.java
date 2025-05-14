package datastreams_knu.bigpicture.board.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import datastreams_knu.bigpicture.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "comments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long commentIdx;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "board_idx", nullable = false)
    @JsonBackReference
    private Board board;

    @Column(nullable = false) //columnDefinition = "TEXT"
    private String comment;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String commentPassword;
}