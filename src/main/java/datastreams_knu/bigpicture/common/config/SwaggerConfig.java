package datastreams_knu.bigpicture.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        log.info("Loading Swagger features...");
        return new OpenAPI()
            .components(new Components())
            .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
            .title("2025 산학협력프로젝트1 - 경북대 5팀 - BigPicture Service 명세서")
            .description("[Team GitHub 바로가기](https://github.com/knu-datastreams)")
            .version("1.0.0");
    }
}
