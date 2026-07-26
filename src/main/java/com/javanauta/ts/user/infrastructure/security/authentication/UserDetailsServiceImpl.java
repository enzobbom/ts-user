package com.javanauta.ts.user.infrastructure.security.authentication;

import com.javanauta.ts.user.application.ports.out.persistence.UserPersister;
import com.javanauta.ts.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserPersister userPersister;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userPersister.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        return new SecurityUser(user);
    }
}
