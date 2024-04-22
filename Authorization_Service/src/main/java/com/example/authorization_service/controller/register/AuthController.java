package com.example.authorization_service.controller.register;

import com.example.authorization_service.service.token.TokenService;
import com.example.authorization_service.service.token.TokenValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@Tag(name = "Authentication", description = "Endpoints for user authentication")
public class AuthController {

    private final TokenService tokenService;
    private final TokenValidationService validationService;

    @Autowired
    public AuthController(TokenService tokenService, TokenValidationService validationService) {
        this.tokenService = tokenService;
        this.validationService = validationService;
    }

    @Operation(summary = "Login to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token generated")
    })
    @PostMapping("/signin")
    public String login(Authentication authentication) {
        return tokenService.generateToken(authentication);
    }

    @Operation(summary = "Check user role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role checked"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/check")
    public String checkRole(@RequestBody String token) {
        return validationService.validateToken(token);
    }
}
