package datastreams_knu.bigpicture.alert.repository;

import datastreams_knu.bigpicture.alert.entity.MemberWatchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberWatchlistRepository extends JpaRepository<MemberWatchlist, Long> {
    boolean existsByMemberIdAndWatchlistId(String memberId, Long watchlistId);

    @Modifying
    @Query("""
            DELETE FROM MemberWatchlist mw
            WHERE mw.member.id=:memberId
            AND mw.watchlist.stockName=:watchlistStockName
            """)
    void deleteByMemberIdAndWatchlistStockName(@Param("memberId") String memberId, @Param("watchlistStockName") String watchlistStockName);
}
