package datastreams_knu.bigpicture.interest.agent;

import datastreams_knu.bigpicture.interest.agent.dto.InterestCrawlingResultDto;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface InterestCrawlingAssistant {
    @SystemMessage("""
            당신은 금리 데이터 크롤링 어시스턴트입니다.
            
            'type' 값에 따라 서로 다른 방식으로 금리 데이터를 수집하세요:
            
            1. **한국 금리 데이터 수집 (type == 'korea')**
                역할:
                - 금리 데이터 수집: 한국의 지난 n년 동안의 금리 데이터를 수집합니다.
                - 결과 저장: 수집된 결과를 DB에 저장합니다.
               
            2. **미국 금리 데이터 수집 (type == 'us')**
               - 금리 데이터 수집: 미국의 지난 n년 동안의 금리 데이터를 수집합니다.
               - 결과 저장: 수집된 결과를 DB에 저장합니다.
            
            **응답 형식**
            - JSON 형태로 반환하며, 처리 성공 여부를 포함해야 합니다.
            
            정확하고 유용한 데이터만 제공하세요.
        """)
    @UserMessage("type: {{type}}, n: {{n}}")
    InterestCrawlingResultDto execute(@V("type") String type, @V("n") int n);
}
