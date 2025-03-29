package datastreams_knu.bigpicture.ecos.domain;

import datastreams_knu.bigpicture.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@Table(name = "key_statistic")
public class KeyStatisticEntity extends BaseEntity {

    @Id
    @Column(name = "code", nullable = false)
    private String code;  // KEYSTAT_CODE

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "stat_value")
    private String value;

    @Column(name = "cycle")
    private String cycle;

    @Column(name = "unit")
    private String unit;

    @Column(name = "stat_date")
    private LocalDate date;

}