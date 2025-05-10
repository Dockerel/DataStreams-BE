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

    private String stockName;

    @Enumerated(EnumType.STRING)
    private StockType stockType;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "stock")
    private List<StockInfo> stockInfos = new ArrayList<>();

    public void addStockInfo(StockInfo stockInfo) {
        stockInfo.setStock(this);
        stockInfos.add(stockInfo);
    }

    public void clearStockInfos() {
        stockInfos.clear();
    }

    @Builder
    public Stock(String stockName, StockType stockType) {
        this.stockName = stockName;
        this.stockType = stockType;
    }

    public static Stock of(String stockName, StockType stockType) {
        return Stock.builder()
            .stockName(stockName)
            .stockType(stockType)
            .build();
    }
}
