package datastreams_knu.bigpicture.stock.agent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class KoreaStockCrawlingDto {
    private Response response;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private Body body;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Body {
        private Items items;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Items {
        private List<Item> item;
    }

    /**
     * basDt: 기준일자
     * clpr: 종가
     */
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Item {
        private String basDt;
        private String clpr;

        public static Item of(String basDt, String clpr) {
            return Item.builder()
                .basDt(basDt)
                .clpr(clpr)
                .build();
        }
    }

    public static KoreaStockCrawlingDto of(List<Item> itemList) {
        Items items = new Items(itemList);
        Body body = new Body(items);
        Response response = new Response(body);

        return KoreaStockCrawlingDto.builder()
            .response(response)
            .build();
    }
}