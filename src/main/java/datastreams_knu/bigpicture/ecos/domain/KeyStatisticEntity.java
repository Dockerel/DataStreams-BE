package datastreams_knu.bigpicture.ecos.domain;

import datastreams_knu.bigpicture.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KeyStatisticEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stat_code")
    private String stat_code;

    @Column(name = "stat_name", nullable = false)
    private String stat_name;

    @Column(name = "stat_value")
    private String stat_value;

    @Column(name = "stat_cycle")
    private String stat_cycle;

    @Column(name = "stat_unit")
    private String stat_unit;

    @Column(name = "stat_date")
    private String stat_date;

}