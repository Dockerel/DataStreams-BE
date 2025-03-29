package datastreams_knu.bigpicture.fred.controller;

import datastreams_knu.bigpicture.common.domain.ApiResponse;
import datastreams_knu.bigpicture.fred.domain.FredSeriesSearchResponse;
import datastreams_knu.bigpicture.fred.service.FredApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fred")
@RequiredArgsConstructor
public class FredApiController {

    private final FredApiService fredApiService;

    @GetMapping("/search")
    public ApiResponse<FredSeriesSearchResponse> searchSeries(@RequestParam String searchText) {
        return fredApiService.searchSeries(searchText);
    }
}
//검색 유형
//수행할 검색 유형을 결정합니다.
//다음 문자열 중 하나: 'full_text', 'series_id'.
//        'full_text'는 단어를 스템으로 구문 분석하여 시리즈 속성 제목, 단위, 빈도 및 태그를 검색합니다. 이를 통해 'Industry'와 같은 검색이 'Industries'와 같은 관련 단어가 포함된 시리즈와 일치할 수 있습니다. 물론 'money' 및 'stock'과 같이 여러 단어를 검색할 수 있습니다. 공백을 URL 인코딩하는 것을 잊지 마세요(예: 'money%20stock').
//        'series_id'는 시리즈 ID에 대한 하위 문자열 검색을 수행합니다. 'ex'를 검색하면 시리즈 ID의 어느 곳에서나 'ex'가 포함된 시리즈를 찾습니다. '*'는 검색을 앵커링하고 0개 이상의 모든 문자와 일치시키는 데 사용할 수 있습니다. 'ex*'를 검색하면 시리즈 ID의 시작 부분에 'ex'가 포함된 시리즈를 찾습니다. '*ex'를 검색하면 시리즈 ID의 끝에 'ex'가 포함된 시리즈를 찾습니다. 문자열 중간에 '*'를 넣을 수도 있습니다. 'm*sl'은 'm'으로 시작하고 'sl'로 끝나는 모든 시리즈를 찾습니다.
//        선택 사항, 기본값: full_text.