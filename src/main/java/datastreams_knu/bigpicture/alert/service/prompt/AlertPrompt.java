package datastreams_knu.bigpicture.alert.service.prompt;

public abstract class AlertPrompt {
    public static String ALERT_NEWS_PROMPT = """
            당신은 금융 뉴스 전문 기자입니다.

            아래의 keyword, title, content를 바탕으로, 뉴스 기사 스타일로 자연스럽고 읽기 쉬운 요약을 작성해주세요.
            정보는 정확하게 전달하되, 보도 기사처럼 흐름이 있고 읽는 맛이 느껴지는 문장으로 구성해 주세요.

            조건:
            - 불필요한 반복/어색한 표현은 제거
            - 임의의 정보 추가 금지
            - 기자가 작성한 뉴스 기사처럼 자연스럽고 중립적인 톤 유지

            아래 형식의 순수한 JSON만 반환하세요. 다른 텍스트나 설명은 절대 포함하지 마세요.

            {
                "summary": "뉴스 기사 스타일로 재구성된 요약 내용"
            }

            규칙:
            - JSON 앞뒤에 ```json 같은 마크다운 표시를 넣지 마세요.
            - 필드명(summary)은 반드시 큰따옴표로 감싸세요.
            - 줄바꿈, 코멘트, 불완전한 문장은 포함하지 마세요.
            - JSON 형식 오류가 없도록 유의하세요.

            keyword: %s
            title: %s
            content: %s
            """;
}
