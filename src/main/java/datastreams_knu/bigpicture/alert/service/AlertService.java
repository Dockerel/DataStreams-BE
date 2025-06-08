package datastreams_knu.bigpicture.alert.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import datastreams_knu.bigpicture.alert.entity.Member;
import datastreams_knu.bigpicture.alert.entity.Watchlist;
import datastreams_knu.bigpicture.alert.repository.MemberRepository;
import datastreams_knu.bigpicture.alert.repository.MemberWatchlistRepository;
import datastreams_knu.bigpicture.alert.repository.WatchlistRepository;
import datastreams_knu.bigpicture.alert.service.dto.AlertNewsResponse;
import datastreams_knu.bigpicture.alert.service.dto.DeleteWatchlistServiceRequest;
import datastreams_knu.bigpicture.alert.service.dto.RegisterFcmTokenServiceRequest;
import datastreams_knu.bigpicture.alert.service.dto.RegisterWatchlistServiceRequest;
import datastreams_knu.bigpicture.common.exception.ObjectMapperException;
import datastreams_knu.bigpicture.common.util.StockNameValidator;
import datastreams_knu.bigpicture.common.util.TickerParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AlertService {

    private final MemberRepository memberRepository;
    private final WatchlistRepository watchlistRepository;
    private final MemberWatchlistRepository memberWatchlistRepository;

    private final StockNameValidator stockNameValidator;
    private final TickerParser tickerParser;

    private final FcmService fcmService;
    private final ObjectMapper objectMapper;

    @Transactional
    public String registerFcmToken(RegisterFcmTokenServiceRequest request) {
        Member member = memberRepository.findById(request.getUuid())
                .orElseGet(() -> memberRepository.save(Member.from(request)));
        if (!member.getFcmToken().equals(request.getFcmToken())) {
            member.updateFcmToken(request.getFcmToken());
        }
        return "FCM 토큰이 등록되었습니다.";
    }

    public List<String> getMyWatchlist(String uuid) {
        Member member = memberRepository.findWithWatchlistsById(uuid)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 uuid 입니다."));

        return member.getMemberWatchlists().stream()
                .map(memberWatchlist -> memberWatchlist.getWatchlist().getStockName())
                .collect(Collectors.toList());
    }

    @Transactional
    public String registerWatchlist(RegisterWatchlistServiceRequest request) {
        Member member = getMember(request.getUuid());

        String stockName = request.getStockName();
        String stockType = request.getStockType();

        Watchlist watchlist = getWatchlist(stockName, stockType);

        if (memberWatchlistNotExists(member, watchlist)) {
            member.addMemberWatchlist(watchlist);
        }

        return "관심 뉴스 등록이 완료되었습니다.";
    }

    @Transactional
    public String deleteWatchlist(DeleteWatchlistServiceRequest request) {
        memberWatchlistRepository.deleteByMemberIdAndWatchlistStockName(request.getUuid(), request.getStockName());
        return "관심 뉴스 삭제가 완료되었습니다.";
    }

    private boolean memberWatchlistNotExists(Member member, Watchlist watchlist) {
        return !memberWatchlistRepository.existsByMemberIdAndWatchlistId(member.getId(), watchlist.getId());
    }

    private Watchlist getWatchlist(String stockName, String stockType) {
        return watchlistRepository.findByStockName(stockName)
                .orElseGet(() -> {
                    if (stockNameValidator.isInvalidStockName(stockName, stockType)) {
                        throw new IllegalArgumentException("유효하지 않은 stockName 입니다.");
                    }

                    String stockKeyword = stockType.equals("korea") ? stockName : tickerParser.parseTicker(stockName);
                    ;
                    Watchlist newWatchlist = Watchlist.of(stockName, stockKeyword);
                    return watchlistRepository.save(newWatchlist);
                });
    }

    private Member getMember(String id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 uuid 입니다."));
        return member;
    }

    public String sendTestFcmRequest() {
        List<Member> members = memberRepository.findAll();
        members.stream()
                .forEach(member -> {
                    String fcmToken = member.getFcmToken();
                    AlertNewsResponse body = AlertNewsResponse.of(
                            LocalDateTime.now(),
                            "https://www.yna.co.kr/view/AKR20250523021800017",
                            "삼성전자",
                            "초슬림형 '갤S25 엣지' 국내 출시…\"사전판매서 젊은 세대 관심\""
                    );
                    String bodyString = null;
                    try {
                        bodyString = objectMapper.writeValueAsString(body);
                    } catch (JsonProcessingException e) {
                        throw new ObjectMapperException("직렬화 중 예외가 발생하였습니다.", e);
                    }
                    fcmService.sendMessageTo(fcmToken, "Test Notification", bodyString);
                });
        return "test fcm requested";
    }
}
