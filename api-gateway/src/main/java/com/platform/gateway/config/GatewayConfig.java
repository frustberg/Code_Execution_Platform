package com.platform.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Auth Service Routes (No JWT required, No rate limiting for auth)
                .route("auth-service", r -> r
                        .path("/api/auth/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri("http://localhost:8081"))
                
                // Backend Service Routes (JWT required)
                .route("backend-problems", r -> r
                        .path("/api/problems/**")
                        .filters(f -> f
                                .stripPrefix(0)
                                .filter(jwtAuthenticationFilter().apply(new com.platform.gateway.filter.JwtAuthenticationFilter.Config()))
                        )
                        .uri("http://localhost:8082"))
                
                .route("backend-submissions", r -> r
                        .path("/api/submissions/**")
                        .filters(f -> f
                                .stripPrefix(0)
                                .filter(jwtAuthenticationFilter().apply(new com.platform.gateway.filter.JwtAuthenticationFilter.Config()))
                        )
                        .uri("http://localhost:8082"))
                
                .route("backend-leaderboard", r -> r
                        .path("/api/leaderboard/**")
                        .filters(f -> f
                                .stripPrefix(0)
                                .filter(jwtAuthenticationFilter().apply(new com.platform.gateway.filter.JwtAuthenticationFilter.Config()))
                        )
                        .uri("http://localhost:8082"))
                
                .build();
    }

    @Bean
    public com.platform.gateway.filter.JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new com.platform.gateway.filter.JwtAuthenticationFilter();
    }
}
