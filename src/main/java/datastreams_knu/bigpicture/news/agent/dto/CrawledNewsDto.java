package datastreams_knu.bigpicture.news.agent.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CrawledNewsDto {
    public YibKrA YIB_KR_A;

    @Getter
    public static class YibKrA {
        public List<Result> result;
    }

    @Getter
    public static class Result {
        public String CID;
    }
}
