package datastreams_knu.bigpicture.ecos.repository;

import datastreams_knu.bigpicture.ecos.domain.KeyStatisticEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.time.LocalDate;

@Repository
public interface KeyStatisticRepository extends JpaRepository<KeyStatisticEntity, String> {
    Optional<KeyStatisticEntity> findByCode(String code);
    List<KeyStatisticEntity> findByNameContaining(String name);
    List<KeyStatisticEntity> findByDateBetween(LocalDate startDate, LocalDate endDate);
}