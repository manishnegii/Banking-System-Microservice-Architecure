package com.rexchord.common_security.autoconfig;

import com.rexchord.common_security.config.JwtProperties;
import com.rexchord.common_security.filter.JwtAuthFilter;
import com.rexchord.common_security.util.JwtUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class CommonSecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JwtUtils jwtUtils(JwtProperties properties) {
        return new JwtUtils(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtAuthFilter jwtAuthenticationFilter(JwtUtils jwtUtils) {
        return new JwtAuthFilter(jwtUtils);
    }
}
