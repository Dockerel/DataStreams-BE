package datastreams_knu.bigpicture.alert.repository;

import datastreams_knu.bigpicture.alert.entity.Member;
import datastreams_knu.bigpicture.alert.service.AlertService;
import datastreams_knu.bigpicture.alert.service.dto.GetMyWatchlistServiceRequest;
import datastreams_knu.bigpicture.alert.service.dto.RegisterWatchlistServiceRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    WatchlistRepository watchlistRepository;
    @Autowired
    AlertService alertService;

    @DisplayName("MemberRepository에서 String으로 정의한 id로 Member를 찾아온다.")
    @Test
    void findByIdTest() {
        // given
        String id = "uuid";
        String fcmToken = "fcmToken";
        Member member = Member.of(id, fcmToken);

        memberRepository.save(member);

        // when
        Optional<Member> findMember = memberRepository.findById(id);

        // then
        assertThat(findMember).isPresent();
        assertThat(findMember.get().getId()).isEqualTo(id);
        assertThat(findMember.get().getFcmToken()).isEqualTo(fcmToken);
    }

    @Commit
    @DisplayName("")
    @Test
    void test() {
        String id = "uuid";
        String fcmToken = "fcmToken";
        Member member = Member.of(id, fcmToken);

        memberRepository.save(member);

        alertService.registerWatchlist(RegisterWatchlistServiceRequest.of(id, "삼성전자", "korea"));

        alertService.registerWatchlist(RegisterWatchlistServiceRequest.of(id, "삼성전자", "korea"));

        alertService.registerWatchlist(RegisterWatchlistServiceRequest.of(id, "LG디스플레이", "korea"));

        List<String> watchlists = alertService.getMyWatchlist(GetMyWatchlistServiceRequest.of(id));
        for (String watchlist : watchlists) {
            System.out.println("watchlist = " + watchlist);
        }
    }
}