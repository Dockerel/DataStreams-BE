package datastreams_knu.bigpicture.interest.repository;

import datastreams_knu.bigpicture.interest.domain.KoreaInterest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KoreaInterestRepository extends JpaRepository<KoreaInterest, Long> {
}
