package datastreams_knu.bigpicture.alert.entity;

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
public class Watchlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stockName;
    private String stockKeyword;

    @OneToMany(mappedBy = "watchlist", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<MemberWatchlist> memberWatchlists = new ArrayList<>();

    public void addMemberWatchlist(MemberWatchlist memberWatchlist) {
        memberWatchlists.add(memberWatchlist);
    }

    @Builder
    public Watchlist(String stockName, String stockKeyword) {
        this.stockName = stockName;
        this.stockKeyword = stockKeyword;
    }

    public static Watchlist of(String stockName, String stockKeyword) {
        return Watchlist.builder()
                .stockName(stockName)
                .stockKeyword(stockKeyword)
                .build();
    }
}
