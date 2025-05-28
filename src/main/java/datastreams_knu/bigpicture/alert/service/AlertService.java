package datastreams_knu.bigpicture.alert.service;

import datastreams_knu.bigpicture.alert.entity.Member;
import datastreams_knu.bigpicture.alert.entity.Watchlist;
import datastreams_knu.bigpicture.alert.repository.MemberRepository;
import datastreams_knu.bigpicture.alert.repository.MemberWatchlistRepository;
import datastreams_knu.bigpicture.alert.repository.WatchlistRepository;
import datastreams_knu.bigpicture.alert.service.dto.DeleteWatchlistServiceRequest;
import datastreams_knu.bigpicture.alert.service.dto.GetMyWatchlistServiceRequest;
import datastreams_knu.bigpicture.alert.service.dto.RegisterFcmTokenServiceRequest;
import datastreams_knu.bigpicture.alert.service.dto.RegisterWatchlistServiceRequest;
import datastreams_knu.bigpicture.common.util.StockNameValidator;
import datastreams_knu.bigpicture.common.util.StockKeywordResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final StockKeywordResolver stockKeywordResolver;

    @Transactional
    public String registerFcmToken(RegisterFcmTokenServiceRequest request) {
        Member member = memberRepository.findById(request.getUuid())
                .orElseGet(() -> memberRepository.save(Member.from(request)));
        if (!member.getFcmToken().equals(request.getFcmToken())) {
            member.updateFcmToken(request.getFcmToken());
        }
        return "FCM 토큰이 등록되었습니다.";
    }

    public List<String> getMyWatchlist(GetMyWatchlistServiceRequest request) {
        Member member = memberRepository.findWithWatchlistsById(request.getUuid())
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

                    String stockKeyword = stockType.equals("korea") ? stockName : stockKeywordResolver.resolve(stockName);
                    Watchlist newWatchlist = Watchlist.of(stockName, stockKeyword);
                    return watchlistRepository.save(newWatchlist);
                });
    }

    private Member getMember(String id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 uuid 입니다."));
        return member;
    }
}
