package datastreams_knu.bigpicture.interest.repository;

import datastreams_knu.bigpicture.interest.entity.KoreaInterest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface KoreaInterestRepository extends JpaRepository<KoreaInterest, Long> {
    int deleteAllByInterestDateBefore(LocalDate localDate);
}
