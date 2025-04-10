package datastreams_knu.bigpicture.interest.agent.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DateRangeDto {
    private String startYear;
    private String endYear;
}
