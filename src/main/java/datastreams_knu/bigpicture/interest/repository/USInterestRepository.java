package datastreams_knu.bigpicture.interest.repository;

import datastreams_knu.bigpicture.interest.domain.KoreaInterest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestRepository extends JpaRepository<KoreaInterest, Long> {
}
