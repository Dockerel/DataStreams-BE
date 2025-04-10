package datastreams_knu.bigpicture.schedule.controller.dto;

import datastreams_knu.bigpicture.schedule.service.dto.RegisterCrawlingDataServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RegisterCrawlingDataRequest {

    @NotBlank
    @Pattern(regexp = "^(korea|us)$", message = "stockType은 'korea' 또는 'us'만 허용됩니다.")
    private String stockType;
    @NotBlank
    private String stockName;

    public RegisterCrawlingDataServiceRequest toServiceRequest() {
        return RegisterCrawlingDataServiceRequest.of(stockType, stockName);
    }

    @Builder
    public RegisterCrawlingDataRequest(String stockType, String stockName) {
        this.stockType = stockType;
        this.stockName = stockName;
    }

    public static RegisterCrawlingDataRequest of(String stockType, String stockName) {
        return RegisterCrawlingDataRequest.builder()
            .stockType(stockType)
            .stockName(stockName)
            .build();
    }
}
