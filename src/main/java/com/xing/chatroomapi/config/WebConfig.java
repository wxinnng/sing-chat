package com.xing.chatroomapi.config;

import com.xing.chatroomapi.interceptor.JwtTokenInterceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;

/**
 * @Author:WangXing
 * @DATE:2024/4/26 14:17
 * @DESCRIPTION: Web 配置类
 * @VERSION:1.0
 */
@SuppressWarnings("all")
@Configuration
@Slf4j
public class WebConfig extends WebMvcConfigurationSupport {


    @Autowired
    private JwtTokenInterceptor jwtTokenInterceptor;


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //跨域配置所有的域名ip都可以
        registry.addMapping("/**").allowedOrigins("*");
    }

    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    protected void addInterceptors(InterceptorRegistry registry) {
        //自定义需要拦截的内容即可
        log.info("开始注册自定义拦截器...");
        ArrayList<String> path = new ArrayList<>();
        path.add("/user/login");
        path.add("/user/register");
        registry.addInterceptor(jwtTokenInterceptor)
                .addPathPatterns("/user/**")
                .addPathPatterns("/test/**")
                .excludePathPatterns(path);
    }

    /**
     * 通过knife4j生成接口文档
     */
    @Bean
    public Docket docket() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("chatroom接口文档")
                .version("1.0")
                .description(" chatroom")
                .build();
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.xing.chatroomapi.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 设置静态资源映射
     */
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }



}
