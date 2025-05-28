package datastreams_knu.bigpicture.alert.repository;

import datastreams_knu.bigpicture.alert.entity.Member;
import datastreams_knu.bigpicture.alert.service.AlertService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
}