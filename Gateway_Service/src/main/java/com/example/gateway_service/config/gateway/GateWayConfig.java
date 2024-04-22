package com.example.gateway_service.config.gateway;

import com.example.gateway_service.service.TokenCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * The GateWayConfig class configures the routes for the gateway service.
 * It defines route rules using RouteLocatorBuilder and applies token validation filters to secure certain routes.
 */
@Configuration
public class GateWayConfig {

    private final TokenCheckService service;
    /**
     * Constructs a new GateWayConfig with the specified TokenCheckService.
     *
     * @param service The service for token check.
     */
    @Autowired
    public GateWayConfig(TokenCheckService service) {
        this.service = service;
    }

    /**
     * Configures custom routes for the gateway service.
     *
     * @param builder The RouteLocatorBuilder used to build route locators.
     * @param tokenValidationFilter The token validation filter for securing routes.
     * @return The RouteLocator containing the configured routes.
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder, TokenValidationFilter tokenValidationFilter) {
        return builder.routes()
                .route("registration-route", r -> r.path("/register/**")
                        .uri("http://app-user:8085"))

                .route("auth-route", r -> r.path("/login/**")
                        .uri("http://app-user:8085"))

                .route("user-route", r -> r.path("/users/**")
                        .filters(f -> f.filter(tokenValidationFilter.apply(new TokenValidationFilter(service))))
                        .uri("http://app-user:8085"))

                .route("crew-route", r -> r.path("/crew/**")
                        .filters(f -> f.filter(tokenValidationFilter.apply(new TokenValidationFilter(service))))
                        .uri("http://app-crew:8081"))

                .route("vessel-route", r -> r.path("/vessel/**")
                        .filters(f -> f.filter(tokenValidationFilter.apply(new TokenValidationFilter(service))))
                        .uri("http://app-vessel:8082"))

                .route("voyage-route", r -> r.path("/voyage/**")
                        .filters(f -> f.filter(tokenValidationFilter.apply(new TokenValidationFilter(service))))
                        .uri("http://app-voyage:8083"))
                .build();
    }
}