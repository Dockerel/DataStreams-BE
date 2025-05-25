package datastreams_knu.bigpicture.alert.entity;

import datastreams_knu.bigpicture.alert.service.dto.RegisterFcmTokenServiceRequest;
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
public class Member {

    @Id
    private String id;

    private String fcmToken;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<MemberWatchlist> memberWatchlists = new ArrayList<>();

    public void addMemberWatchlist(Watchlist watchlist) {
        MemberWatchlist memberWatchlist = MemberWatchlist.of(this, watchlist);
        this.memberWatchlists.add(memberWatchlist);
        watchlist.getMemberWatchlists().add(memberWatchlist);
    }

    @Builder
    public Member(String id, String fcmToken) {
        this.id = id;
        this.fcmToken = fcmToken;
    }

    public static Member of(String id, String fcmToken) {
        return Member.builder()
                .id(id)
                .fcmToken(fcmToken).build();
    }

    public static Member from(RegisterFcmTokenServiceRequest request) {
        return Member.of(request.getUuid(), request.getFcmToken());
    }

    public void updateFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
