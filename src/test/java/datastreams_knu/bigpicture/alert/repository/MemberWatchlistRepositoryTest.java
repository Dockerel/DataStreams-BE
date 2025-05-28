package datastreams_knu.bigpicture.alert.repository;

import datastreams_knu.bigpicture.alert.entity.Member;
import datastreams_knu.bigpicture.alert.service.AlertService;
import datastreams_knu.bigpicture.alert.service.dto.RegisterWatchlistServiceRequest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MemberWatchlistRepositoryTest {

    @Autowired
    MemberWatchlistRepository memberWatchlistRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    AlertService alertService;
    @Autowired
    EntityManager em;

    @Commit
    @DisplayName("")
    @Test
    void test() {
        String id = "uuid";
        String fcmToken = "fcmToken";
        Member member = Member.of(id, fcmToken);

        memberRepository.save(member);

        String stockName = "삼성전자";
        alertService.registerWatchlist(RegisterWatchlistServiceRequest.of(id, stockName, "korea"));

        alertService.registerWatchlist(RegisterWatchlistServiceRequest.of(id, stockName, "korea"));

        alertService.registerWatchlist(RegisterWatchlistServiceRequest.of(id, "LG디스플레이", "korea"));

        memberWatchlistRepository.deleteByMemberIdAndWatchlistStockName(id, stockName);
    }

}