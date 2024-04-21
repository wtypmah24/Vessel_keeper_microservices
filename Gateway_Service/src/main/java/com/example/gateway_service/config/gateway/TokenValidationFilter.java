package com.example.gateway_service.config.gateway;

import com.example.gateway_service.service.TokenCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.nio.file.AccessDeniedException;

/**
 * The TokenValidationFilter class is a GatewayFilter used for token validation and access control in the gateway service.
 * It extracts the token from the request, sends it to the TokenCheckService for validation, and checks access based on the user's role.
 */
@Component
public class TokenValidationFilter extends AbstractGatewayFilterFactory<TokenValidationFilter> {
    private final TokenCheckService service;

    /**
     * Constructs a new TokenValidationFilter with the specified TokenCheckService.
     *
     * @param service The service for token check.
     */
    @Autowired
    public TokenValidationFilter(TokenCheckService service) {
        this.service = service;
    }

    /**
     * Applies the token validation filter to the gateway route.
     *
     * @param config The configuration for the token validation filter.
     * @return The GatewayFilter for token validation and access control.
     */
    @Override
    public GatewayFilter apply(TokenValidationFilter config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String token = extractTokenFromRequest(request);
            if (token != null) {
                String role = sendTokenToAuthService(token);
                if (hasAccess(role, request)) {
                    return chain.filter(exchange);
                } else {
                    return Mono.error(new AccessDeniedException("Access denied"));
                }
            } else {
                return Mono.error(new IllegalArgumentException("Token not found"));
            }
        };
    }

    /**
     * Checks if the user with the specified role has access to the requested resource.
     *
     * @param role    The role of the user.
     * @param request The server HTTP request.
     * @return True if the user has access, false otherwise.
     */
    private boolean hasAccess(String role, ServerHttpRequest request) {
        if ("OWNER".equals(role)) {
            return true;
        } else if ("CREW_MANAGER".equals(role) && request.getPath().toString().startsWith("/crew")) {
            return true;
        } else if ("OPERATIONAL_MANAGER".equals(role) && request.getPath().toString().startsWith("/voyage")) {
            return true;
        } else return "OPERATIONAL_MANAGER".equals(role) && request.getPath().toString().startsWith("/vessel");
    }

    /**
     * Extracts the token from the request's authorization header.
     *
     * @param request The server HTTP request.
     * @return The extracted token, or null if not found.
     */
    public String extractTokenFromRequest(ServerHttpRequest request) {
        String authorizationHeader = request.getHeaders().getFirst("Authorization");
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    /**
     * Sends the token to the TokenCheckService for validation and retrieves the user's role.
     *
     * @param token The token to be validated.
     * @return The role of the user associated with the token.
     */
    public String sendTokenToAuthService(String token) {
        return service.getRole(token);
    }
}