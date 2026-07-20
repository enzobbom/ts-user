package com.javanauta.ts.user.infrastructure.security;

import com.javanauta.ts.user.application.ports.out.persistence.UserPersister;
import com.javanauta.ts.user.domain.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserPersister userPersister;

    public UserDetailsServiceImpl(UserPersister userPersister) {
        this.userPersister = userPersister;
    }

    // Loads user details by email
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Searches for user in the database by email
        User user = userPersister.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        // Creates and returns UserDetails based on the found user
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail()) // Sets the username as the email
                .password(user.getPassword()) // Sets the user's password
                .build(); // Builds the UserDetails object
    }
}
