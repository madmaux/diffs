package org.mq.diff.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 *
 * SwaggerConfig
 *
 * @author swagger.io
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("org.mq.diff.controller"))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(apiInfo()).useDefaultResponseMessages(false);
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("Waes Diff")
        .description(
            "Scalable web-app developed in java using spring-boot that compares two base64 binaries")
        .contact(new Contact("Mauricio Quezada", null, "mauro.queur@gmail.com"))
        .version("0.0.1")
        .build();
  }
}
