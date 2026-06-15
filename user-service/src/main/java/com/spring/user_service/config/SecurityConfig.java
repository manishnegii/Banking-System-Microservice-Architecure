package com.spring.user_service.config;


import com.rexchord.common_security.filter.JwtAuthFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(c-> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)


                .authorizeHttpRequests( auth -> auth
                        .requestMatchers("/api/v1/customers/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/v1/customers-docs/*/kyc-document").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.POST,"/api/v1/customers-docs/*/approve").hasAnyAuthority("USER","AUDITOR")
                        .requestMatchers(HttpMethod.POST,"/api/v1/customers-docs/*/reject").hasAnyAuthority("USER","AUDITOR")
                        .requestMatchers(HttpMethod.POST,"/api/v1/customers-docs/*/review").hasAnyAuthority("USER","AUDITOR")

                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling(c -> {
                    c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
                    c.accessDeniedHandler(((request, response, accessDeniedException) ->
                            response.setStatus(HttpStatus.FORBIDDEN.value())));});


        return http.build();
    }
}