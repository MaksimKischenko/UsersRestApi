package com.example.user_api_archives.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.List;


@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@NotNull CorsRegistry registry) {
        registry.addMapping("/**");
    }

    @Override
    public void configureContentNegotiation(@NotNull ContentNegotiationConfigurer configurer) {
        configurer.favorParameter(true).defaultContentTypeStrategy(
                webRequest -> {
                    String mediaType = webRequest.getHeader(HttpHeaders.CONTENT_TYPE);
                    if (mediaType != null) {
                        if (mediaType.contains(MediaType.APPLICATION_JSON_VALUE)) {
                            return List.of(MediaType.APPLICATION_JSON);
                        }
                    }
                    return List.of(MediaType.ALL);
                }).ignoreAcceptHeader(true);
    }

    @Override
    public void configureMessageConverters(@NotNull List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter(objectMapper()));
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.enable(SerializationFeature.WRAP_ROOT_VALUE);
        om.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return om;
    }

    @Bean("testString")
    public String testString() {
        return "My web config settings";
    }
}
