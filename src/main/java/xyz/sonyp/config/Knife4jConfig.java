package xyz.sonyp.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI userOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("用户管理接口文档")
                        .description("用户接口文档")
                        .contact(new Contact()
                                .name("songyP🔥")
                                .email("2032164545@qq.com")
                                .url("https://xyz.songyp"))
                        .version("v1.0"));
    }
}