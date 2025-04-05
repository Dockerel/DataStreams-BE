package datastreams_knu.bigpicture.stock.agent.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class KoreaStockCrawlingDto {
    private Response response;

    @Getter
    public static class Response {
        private Body body;
    }

    @Getter
    public static class Body {
        private Items items;
    }

    @Getter
    public static class Items {
        private List<Item> item;
    }

    /**
     * basDt: 기준일자
     * clpr: 종가
     */
    @Getter
    public static class Item {
        private String basDt;
        private String clpr;
    }
}