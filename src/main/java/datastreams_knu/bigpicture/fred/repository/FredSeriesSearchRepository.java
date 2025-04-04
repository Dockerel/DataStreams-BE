package datastreams_knu.bigpicture.fred.repository;

import datastreams_knu.bigpicture.fred.domain.FredSeriesSearchResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FredSeriesSearchRepository extends JpaRepository<FredSeriesSearchResponse, Long> {

}
