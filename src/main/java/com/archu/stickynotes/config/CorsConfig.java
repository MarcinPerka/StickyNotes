package com.archu.stickynotes.config;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "cors", ignoreInvalidFields = true)
public class CorsConfig implements WebMvcConfigurer {


    @Setter
    private String[] origins;

    @Bean
    public CorsFilter corsFilter() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(origins));
        configuration.setAllowedMethods(List.of(HttpMethod.HEAD.name(), HttpMethod.OPTIONS.name(), HttpMethod.GET.name(),
                HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name(), HttpMethod.PATCH.name()));

        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must
        // not be the wildcard '*' when the request's credentials mode is 'include'.
        configuration.setAllowCredentials(true);

        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setMaxAge(3600L);

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
    }
}