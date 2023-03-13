package com.paintourcolor.odle.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Bean
    public MultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

    // XSS Filter
//    @Bean
//    public MappingJackson2HttpMessageConverter jsonEscapeConverter() {
//        // MappingJackson2HttpMessageConverter Default ObjectMapper 설정 및 ObjectMapper Config 설정
//        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();
//        objectMapper.getFactory().setCharacterEscapes(new HTMLCharacterEscapes());
//        return new MappingJackson2HttpMessageConverter(objectMapper);
//    }
}