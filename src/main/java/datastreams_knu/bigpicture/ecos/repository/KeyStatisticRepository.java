package datastreams_knu.bigpicture.ecos.repository;

import datastreams_knu.bigpicture.ecos.domain.KeyStatisticEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface KeyStatisticRepository extends JpaRepository<KeyStatisticEntity, Long> {

}