package com.backend.common.config;

import com.backend.common.exception.OauthClientExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class OauthClientConfig {

    @Bean
    RestClient restClient(OauthClientExceptionHandler handler) {
        return RestClient.builder()
                .defaultStatusHandler(handler)
                .build();
    }
}
