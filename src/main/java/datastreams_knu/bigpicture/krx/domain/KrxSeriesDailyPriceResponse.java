package datastreams_knu.bigpicture.krx.domain;

import datastreams_knu.bigpicture.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class KrxSeriesDailyPriceResponse extends BaseEntity {

    @Id
    @Column(name = "bas_dd", nullable = false)
    private String basDd; // 기준일자

    @Column(name = "idx_clss")
    private String idxClss; // 계열구분

    @Column(name = "idx_nm")
    private String idxNm; // 지수명

    @Column(name = "clsprc_idx")
    private String clsprcIdx; // 종가

    @Column(name = "cmpprevdd_idx")
    private String cmpprevddIdx; // 대비

    @Column(name = "fluc_rt")
    private String flucRt; // 등락률

    @Column(name = "opnprc_idx")
    private String opnprcIdx; // 시가

    @Column(name = "hgprc_idx")
    private String hgprcIdx; // 고가

    @Column(name = "lwprc_idx")
    private String lwprcIdx; // 저가

    @Column(name = "acc_trdvol")
    private String accTrdvol; // 거래량

    @Column(name = "acc_trdval")
    private String accTrdval; // 거래대금

    @Column(name = "mktcap")
    private String mktcap; // 상장시가총액
}
