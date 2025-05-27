package datastreams_knu.bigpicture.common.controller;

import datastreams_knu.bigpicture.common.controller.dto.PythonTestDto;
import datastreams_knu.bigpicture.common.domain.ApiResponse;
import datastreams_knu.bigpicture.common.util.WebClientUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/test")
@RestController
@Tag(name = "테스트", description = "테스트용 API")
public class TestController {

    private final WebClientUtil webClientUtil;

    @Value("${python.server.url}")
    private String pythonServerUrl;

    @GetMapping("/spring")
    @Operation(summary = "테스트 엔드포인트 - spring server", description = "스프링 서버 테스트용 엔드포인트")
    public ApiResponse<String> getTestSpring() {
        return ApiResponse.ok("ok");
    }

    @GetMapping("/python")
    @Operation(summary = "테스트 엔드포인트 - python server", description = "파이썬 서버 테스트용 엔드포인트")
    public ApiResponse<String> getTestPython() {
        PythonTestDto res = webClientUtil.get(pythonServerUrl, PythonTestDto.class);
        return ApiResponse.ok(res.getStatus());
    }

}
