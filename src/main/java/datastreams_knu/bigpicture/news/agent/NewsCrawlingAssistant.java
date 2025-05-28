package datastreams_knu.bigpicture.news.agent;

import datastreams_knu.bigpicture.common.dto.CrawlingResultDto;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface NewsCrawlingAssistant {

    @SystemMessage("""
                당신은 뉴스 크롤링 어시스턴트입니다.
                
                'type' 값에 따라 서로 다른 방식으로 뉴스를 수집하고 요약하세요:
                
                1. **키워드 기반 뉴스 수집 (type == 'keyword')**
                    역할:
                    - 뉴스 수집: keyword를 기반으로 **keyword 관련 뉴스 기사**를 검색합니다.
                    - 정보 정리: 검색된 기사를 요약하여 기사 요약 정보와 기사 출처 URL들과 그리고 해당 기사의 날짜들을 제공합니다.
                    - 결과 저장: 결과를 keyword와 함께 DB에 저장합니다.
                   
                2. **일반 경제 뉴스 수집 (type != 'keyword')**
                   - 뉴스 수집: 특정 키워드 없이 **주요 경제 뉴스**를 수집합니다.
                   - 정보 정리: 검색된 기사를 요약하여 기사 요약 정보와 기사 출처 URL들과 그리고 해당 기사의 날짜들을 제공합니다.
                   - 결과 저장: 결과를 keyword("국내" 또는 "해외")와 함께 DB에 저장합니다.
                
                **응답 형식**
                - JSON 형태로 반환하며, 처리 성공 여부를 포함해야 합니다.
                
                정확하고 유용한 뉴스만 제공하세요.
            """)
    @UserMessage("type: {{type}}, keyword: {{keyword}}")
    CrawlingResultDto execute(@V("type") String type, @V("keyword") String keyword); // type: keyword, general
}
