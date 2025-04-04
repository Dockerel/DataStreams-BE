package datastreams_knu.bigpicture.ecos.domain;

import datastreams_knu.bigpicture.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ecos_key_statistic")
public class EcosKeyStatisticResponse extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_date")
    private String requestDate;

    @Column(name = "count")
    private int count;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ecos_key_statistic_id")
    private List<KeyStatistic> row;

    @Getter
    @Setter
    @Entity
    @Table(name = "ecos_key_statistic_item")
    public static class KeyStatistic {

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
}
