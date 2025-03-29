package datastreams_knu.bigpicture.krx.domain;

import datastreams_knu.bigpicture.common.domain.BaseEntity;
import lombok.Data;

@Data
public class KrxSeriesDailyPriceResponse extends BaseEntity {
    private String basDd;
    private String idxClss;
    private String idxNm;
    private String clsprcIdx;
    private String cmpprevddIdx;
    private String flucRt;
    private String opnprcIdx;
    private String hgprcIdx;
    private String lwprcIdx;
    private String accTrdvol;
    private String accTrdval;
    private String mktcap;
}
