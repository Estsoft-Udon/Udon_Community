package com.example.estsoft_udon_community.external.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ExternalApiConfig {
    @Bean
    public RestTemplate restTemplate() {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();

        // JSON 응답 처리를 위한 Jackson 메시지 컨버터 추가
        converters.add(new MappingJackson2HttpMessageConverter());

        // XML 응답 처리를 위한 JAXB 메시지 컨버터 추가
        converters.add(new Jaxb2RootElementHttpMessageConverter());
        return new RestTemplate(converters);
    }
}
