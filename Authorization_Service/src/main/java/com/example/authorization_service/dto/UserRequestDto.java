package com.example.authorization_service.dto;
/**
 * The UserRequestDto record represents the data transfer object (DTO) for creating a user.
 * It contains the user's full name, login, password, and role.
 */
public record UserRequestDto(String fullName, String login, String password, String role) {
}