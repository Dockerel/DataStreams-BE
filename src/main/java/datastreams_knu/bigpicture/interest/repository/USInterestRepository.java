package datastreams_knu.bigpicture.interest.repository;

import datastreams_knu.bigpicture.interest.entity.USInterest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface USInterestRepository extends JpaRepository<USInterest, Long> {
    int deleteAllByInterestDateBefore(LocalDate localDate);
}
