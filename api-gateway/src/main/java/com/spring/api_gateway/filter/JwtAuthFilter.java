package com.spring.api_gateway.filter;

import com.spring.api_gateway.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter implements WebFilter{

    private final JwtUtils jwtUtils;

    private static final List<String> PUBLIC_ROUTES = List.of(
           "/api/v1/auth/login",
           "/api/v1/auth/register",
           "/api/v1/auth/refresh"
    );


    @Override
    public Mono<Void> filter(ServerWebExchange exchange,WebFilterChain chain) {

        System.out.println("Filter hit");

        String path = exchange.getRequest().getURI().getPath();

        if(exchange.getRequest().getMethod() == HttpMethod.OPTIONS){
            return chain.filter(exchange);
        }

        if(isPublicRoute(path)){
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            exchange.getResponse().setRawStatusCode(401);
            return exchange.getResponse().setComplete();
        }

        try{
            String token = authHeader.substring(7);

            if (!jwtUtils.validateToken(token)) {
                exchange.getResponse().setRawStatusCode(401);
                return exchange.getResponse().setComplete();
            }

            String userId = jwtUtils.extractUserId(token);
            String role = jwtUtils.extractRoles(token);

            Authentication authentication = new UsernamePasswordAuthenticationToken(userId,null,List.of(new SimpleGrantedAuthority("ROLE_"+ role)));

            ServerHttpRequest request = exchange
                    .getRequest()
                    .mutate()
                    .header(HttpHeaders.AUTHORIZATION,authHeader)
                    .build();

            ServerWebExchange mutatedExchange = exchange.mutate().request(request).build();

            return chain.filter(mutatedExchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));

//            return  chain.filter(exchange.mutate().request(request).build());

        } catch (Exception e) {
            exchange.getResponse().setRawStatusCode(401);
            return exchange.getResponse().setComplete();
        }
    }

    public boolean isPublicRoute(String path){
        return PUBLIC_ROUTES.stream().anyMatch(path::equals);
    }
}
