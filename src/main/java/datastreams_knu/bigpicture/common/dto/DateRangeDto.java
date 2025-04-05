package datastreams_knu.bigpicture.common.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DateRangeDto {

    private String fromDate;
    private String toDate;

    @Builder
    public DateRangeDto(String fromDate, String toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public static DateRangeDto of(String fromDate, String toDate) {
        return DateRangeDto.builder()
            .fromDate(fromDate)
            .toDate(toDate)
            .build();
    }

    public static DateRangeDto of(String date) {
        return DateRangeDto.of(date, date);
    }
}
