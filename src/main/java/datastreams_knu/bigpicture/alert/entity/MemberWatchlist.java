package datastreams_knu.bigpicture.alert.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MemberWatchlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "watchlist_id")
    private Watchlist watchlist;

    @Builder
    public MemberWatchlist(Member member, Watchlist watchlist) {
        this.member = member;
        this.watchlist = watchlist;
    }

    public static MemberWatchlist of(Member member, Watchlist watchlist) {
        return MemberWatchlist.builder()
                .member(member)
                .watchlist(watchlist)
                .build();
    }
}
