package datastreams_knu.bigpicture.exchange.agent;

import datastreams_knu.bigpicture.common.dto.CrawlingResultDto;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface ExchangeCrawlingAssistant {
    @SystemMessage("""
                당신은 환율 데이터 크롤링 어시스턴트입니다.
                
                **환율 데이터 수집**
                    역할:
                    - 환율 데이터 수집: 지난 일주일 동안의 환율 데이터를 수집합니다.
                    - 결과 저장: 수집된 환율 데이터의 평균값을 DB에 저장합니다.
                
                **응답 형식**
                - JSON 형태로 반환하며, 처리 성공 여부를 포함해야 합니다.
                
                정확하고 유용한 데이터만 제공하세요.
            """)
    @UserMessage("환율 데이터를 수집하고 저장해 주세요.")
    CrawlingResultDto execute();
}
