package com.example.authorization_service.service.user;

import com.example.authorization_service.dto.UserRequestDto;
import com.example.authorization_service.dto.UserResponseDto;
import com.example.authorization_service.entity.user.Role;
import com.example.authorization_service.exception.UserException;
import com.example.authorization_service.mapper.UserMapper;
import com.example.authorization_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * The UserService class provides methods for user management, such as user registration, creation, retrieval, and deletion.
 * It interacts with the UserRepository to perform database operations and uses the UserMapper for mapping between DTOs and entities.
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a new UserService with the specified UserRepository, UserMapper, and PasswordEncoder.
     *
     * @param userRepository  The repository for accessing user data.
     * @param userMapper      The mapper for mapping between DTOs and entities.
     * @param passwordEncoder The encoder for encoding passwords.
     */
    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user with the provided user data.
     *
     * @param userCandidate The DTO containing the user's registration data.
     * @return The DTO containing the newly registered user's information.
     * @throws UserException If there is an error during user registration.
     */
    @Transactional
    public UserResponseDto registration(UserRequestDto userCandidate) throws UserException {
        return createUser(userCandidate);
    }

    /**
     * Creates a new user with the provided user data.
     *
     * @param userCandidate The DTO containing the user's data.
     * @return The DTO containing the newly created user's information.
     * @throws UserException If there is an error during user creation.
     */
    @Transactional
    public UserResponseDto createUser(UserRequestDto userCandidate) throws UserException {
        checkUserCandidate(userCandidate);
        String hashPassword = passwordEncoder.encode(userCandidate.password());

        UserRequestDto newCandidate = new UserRequestDto(userCandidate.fullName(),
                userCandidate.login(),
                hashPassword,
                userCandidate.role());

        return userMapper.userToUserResponseDto(userRepository.save(userMapper.userDtoToUser(newCandidate)));
    }

    /**
     * Retrieves all users from the database.
     *
     * @return A list of DTOs containing information about all users.
     */
    @Transactional
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::userToUserResponseDto).toList();
    }

    /**
     * Deletes the user with the specified login from the database.
     *
     * @param login The login (username) of the user to delete.
     * @throws UserException If the specified user does not exist.
     */
    @Transactional
    public void deleteUser(String login) throws UserException {
        userRepository
                .deleteById(userRepository
                        .findUserByLogin(login)
                        .orElseThrow(() -> new UserException("Can't find user with login: " + login))
                        .getId());
    }

    /**
     * Checks if the provided user candidate is valid.
     *
     * @param candidate The DTO containing the user candidate's data.
     * @throws UserException If the user candidate is invalid.
     */
    private void checkUserCandidate(UserRequestDto candidate) throws UserException {
        if (candidate == null) throw new UserException("You provided empty candidate!");
        if (candidate.login() == null || candidate.login().isBlank())
            throw new UserException("You didn't provide user name");
        if (candidate.password() == null || candidate.password().isBlank())
            throw new UserException("You didn't provide password!");
        if (candidate.role() == null || candidate.role().isBlank())
            throw new UserException("You didn't provide user's role!");
        try {
            Role.valueOf(candidate.role().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UserException("Invalid role specified: " + candidate.role());
        }
    }
}