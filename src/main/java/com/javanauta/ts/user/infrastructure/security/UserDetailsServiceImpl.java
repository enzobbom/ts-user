package com.javanauta.ts.user.infrastructure.security;

import com.javanauta.ts.user.infrastructure.entity.User;
import com.javanauta.ts.user.infrastructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // Repository to access user data from the database
    @Autowired
    private UserRepository userRepository;

    // Loads user details by email
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Searches for user in the database by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        // Creates and returns UserDetails based on the found user
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail()) // Sets the username as the email
                .password(user.getPassword()) // Sets the user's password
                .build(); // Builds the UserDetails object
    }
}
