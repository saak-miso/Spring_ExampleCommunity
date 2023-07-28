package org.example.config;

import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableSwagger2
@EnableWebMvc
public class SwaggerConfig {

    @Bean
    public Docket apiAllService() {

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("00. 전체 API REST Service")
                .apiInfo(apiInfo())
                .select()
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket apiExampleService() {

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("01. 사용자 정보 RestAPI 서비스")
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)   // false로 설정하면
                .host("localhost:8181") // 기본 URL을 명시적으로 지정합니다.
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.example.api.controller")) // API 패키지 이름의 최상위 패키지를 입력하세요
                .paths(PathSelectors.ant("/api/**"))
                .build();
    }

    private ApiInfo apiInfo() {

        return new ApiInfoBuilder()
            .title("SpringFramework DeviceAPI 연계 서비스( Hybrid App )")
            .description("스프링 프레임워크 하이브리드앱 실행 환경 - iOS / Android 하이브리드앱 RestAPI 서비스")
            .termsOfServiceUrl("https://saakmiso.tistory.com/106")
            .contact(new Contact("사악미소", "https://saakmiso.tistory.com/", "wicked@saakmiso.com"))
            .license("Apache License Version 2.0")
            .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
            .version("0.0.1")
            .build();
    }
}