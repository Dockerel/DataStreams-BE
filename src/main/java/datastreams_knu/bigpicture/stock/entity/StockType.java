package datastreams_knu.bigpicture.stock.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StockType {
    KOREA("korea"),
    US("us");

    private String type;
}
