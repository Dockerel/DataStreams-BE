package datastreams_knu.bigpicture.stock.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockInfo> stockInfos = new ArrayList<>();

    public void addStockInfo(StockInfo stockInfo) {
        stockInfos.add(stockInfo);
    }

    public void clearStockInfos() {
        stockInfos.clear();
    }

    @Builder
    public Stock(String name) {
        this.name = name;
    }

    public static Stock of(String name) {
        return Stock.builder()
            .name(name)
            .build();
    }
}
