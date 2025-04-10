package datastreams_knu.bigpicture.schedule.util;

public abstract class TickerParserPrompt {
    public static final String TICKER_PARSER_PROMPT = """
        당신은 금융 도메인 전문가입니다.

        아래에 입력된 해외 기업의 티커(symbol)를 보고, 반드시 해당 기업의 **정식 한국어 회사명**만 정확히 반환해 주세요.

        1. 다른 설명 없이 **정식 한국어 회사명만** 'keyword' 필드에 작성하세요. (예: AAPL → 애플, TSLA → 테슬라)
        2. 키워드는 간결하고 검색용으로 적합해야 하며, 불필요한 글자들(쉼표, 마침표, 따옴표 그리고 Inc 등)은 제외하세요.
        2. 불확실하거나 존재하지 않는 티커면 "unknown"이라고만 답변하세요.

        응답은 반드시 아래 JSON 형식을 따라야 합니다. 다른 형식의 문자는 포함하지 마세요.
        JSON 코드 블록(````json ... `````) 없이 순수한 JSON 데이터만 반환하세요.
        모든 JSON 필드명(`keyword`)과 값은 **반드시 큰따옴표(`"`)로 감싸야 합니다**.

        {
            "keyword": "해외 기업의 티커(symbol)에 해당하는 정식 한국어 회사명"
        }

        티커: %s
        """;
}
