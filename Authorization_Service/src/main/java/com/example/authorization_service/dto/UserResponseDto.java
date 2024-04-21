package com.example.authorization_service.dto;

/**
 * The UserResponseDto record represents the data transfer object (DTO) for retrieving user information.
 * It contains the user's ID, full name, login, and role.
 */
public record UserResponseDto(Long id, String fullName, String login, String role) {
}