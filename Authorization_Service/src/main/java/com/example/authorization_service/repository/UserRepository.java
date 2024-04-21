package com.example.authorization_service.repository;

import com.example.authorization_service.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * The UserRepository interface extends JpaRepository and provides methods for accessing user data in the database.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds a user by their login (username).
     *
     * @param login The login (username) of the user to find.
     * @return An Optional containing the user if found, or an empty Optional otherwise.
     */
    Optional<User> findUserByLogin(String login);
}