package com.serendipity.seity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Cors 관련 설정 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**") // 프로그램에서 제공하는 URL
                .allowedOrigins("https://dev.seity.co.kr", "https://dev.seity.co.kr/**",
                        "http://localhost:3000", "http://localhost:3000/**") // 청을 허용할 출처를 명시
                .allowedHeaders("*") // 어떤 헤더들을 허용할 것인지
                .allowedMethods("*") // 어떤 메서드를 허용할 것인지 (GET, POST 등)
                .allowCredentials(true) // 쿠키 요청을 허용
                .maxAge(3000)
        ;
    }
}
