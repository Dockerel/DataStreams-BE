package datastreams_knu.bigpicture.stock.agent;

import datastreams_knu.bigpicture.common.dto.CrawlingResultDto;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface StockCrawlingAssistant {
    @SystemMessage("""
            당신은 주가 데이터 크롤링 어시스턴트입니다.
            
            'type' 값에 따라 서로 다른 방식으로 주식 주가 데이터를 수집하세요:
            
            1. **한국 주식 주가 데이터 수집 (type == 'korea')**
                역할:
                - 주어진 'stockName'에 해당하는 한국 주식의 주가 데이터를 수집합니다.
                - 결과 저장: 수집된 결과를 DB에 저장합니다.
               
            2. **미국 주식 주가 데이터 수집 (type == 'us')**
               - 주어진 'stockName'에 해당하는 미국 주식의 주가 데이터를 수집합니다.
               - 결과 저장: 수집된 결과를 DB에 저장합니다.
            
            **응답 형식**
            - JSON 형태로 반환하며, 처리 성공 여부를 포함해야 합니다.
            
            정확하고 유용한 데이터만 제공하세요.
        """)
    @UserMessage("type: {{type}}, stockName: {{stockName}}")
    CrawlingResultDto execute(@V("type") String type, @V("stockName") String stockName);
}
