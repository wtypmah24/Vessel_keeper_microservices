package com.example.authorization_service.mapper;

import com.example.authorization_service.dto.UserRequestDto;
import com.example.authorization_service.dto.UserResponseDto;
import com.example.authorization_service.entity.user.Role;
import com.example.authorization_service.entity.user.User;
import org.mapstruct.Mapper;

/**
 * The UserMapper interface defines methods for mapping between UserRequestDto, UserResponseDto, and User entities.
 * It utilizes MapStruct for generating the mapping implementations.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    /**
     * Maps a UserRequestDto object to a User entity.
     *
     * @param userCandidate The UserRequestDto object to map.
     * @return The mapped User entity.
     */
    User userDtoToUser(UserRequestDto userCandidate);

    /**
     * Maps a User entity to a UserResponseDto object.
     *
     * @param user The User entity to map.
     * @return The mapped UserResponseDto object.
     */
    UserResponseDto userToUserResponseDto(User user);

    /**
     * Maps a string representation of a role to a Role enum.
     *
     * @param role The string representation of the role.
     * @return The corresponding Role enum value.
     */
    default Role mapRole(String role) {
        return Role.valueOf(role.toUpperCase());
    }
}
