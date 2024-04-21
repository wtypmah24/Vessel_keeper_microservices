package com.example.authorization_service.service.user;

import com.example.authorization_service.entity.user.UserWithCredentials;
import com.example.authorization_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * The UserSecurityService class implements the UserDetailsService interface to provide user authentication details.
 * It retrieves user details from the UserRepository and wraps them with UserDetails for Spring Security.
 */
@Service
public class UserSecurityService implements UserDetailsService {
    /**
     * The repository for accessing user data.
     */
    private final UserRepository repository;

    /**
     * Constructs a new UserSecurityService with the specified UserRepository.
     *
     * @param repository The repository for accessing user data.
     */
    @Autowired
    public UserSecurityService(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Loads user details by username (login).
     *
     * @param login The username (login) of the user to load.
     * @return The UserDetails object containing the user's authentication details.
     * @throws UsernameNotFoundException If the specified username is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return new UserWithCredentials(repository.findUserByLogin(login).orElseThrow());
    }
}