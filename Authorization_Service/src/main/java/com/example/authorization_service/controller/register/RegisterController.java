package com.example.authorization_service.controller.register;

import com.example.authorization_service.dto.UserRequestDto;
import com.example.authorization_service.dto.UserResponseDto;
import com.example.authorization_service.exception.UserException;
import com.example.authorization_service.service.user.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
@Tag(name = "Registration", description = "Endpoints for user registration")
public class RegisterController {
    private final UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/newuser")
    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<UserResponseDto> registration(@RequestBody UserRequestDto userRequestDto) throws UserException
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registration(userRequestDto));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handleException(UserException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(exception.getMessage()));
    }
}