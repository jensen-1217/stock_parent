package com.jensen.stock.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;


/**
 * @author : itheima
 * @date : 2022/12/15 11:27
 * @description : 定义swagger配置类
 */
@Configuration
@EnableKnife4j
public class SwaggerConfiguration {
   @Bean
   public OpenAPI buildDocket() {
      Contact contact = new Contact()
              .name("jensen")
              .url("https://www.jenseni.com/")
              .email("l594844561@163.com");
      //构建在线API概要对象
      return new OpenAPI()
              .info(new Info().title("股票智汇趋势引擎")
                      .description("股票智汇趋势引擎的API文档")
                      .version("v1")
                      .license(new License().name("访问SpringDoc官方网站").url("http://springdoc.org")).contact(contact));
              /*.externalDocs(new ExternalDocumentation()
                      .description("设计文档")
                      .url("https://www.jenseni.com"));*/

   }
}