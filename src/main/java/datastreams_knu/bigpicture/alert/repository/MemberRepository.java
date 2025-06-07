package datastreams_knu.bigpicture.alert.repository;

import datastreams_knu.bigpicture.alert.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(String id);

    @Query("""
            SELECT m
            FROM Member m
            LEFT JOIN FETCH m.memberWatchlists mw
            LEFT JOIN FETCH mw.watchlist
            WHERE m.id = :id
            """)
    Optional<Member> findWithWatchlistsById(@Param("id") String id);

    void deleteByFcmToken(String fcmToken);
}
