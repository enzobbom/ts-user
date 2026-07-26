package com.javanauta.ts.user.infrastructure.security.password;

import com.javanauta.ts.user.application.ports.out.security.UserPasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringSecurityUserPasswordEncoder implements UserPasswordEncoder {
    private final PasswordEncoder passwordEncoder;

    @Override
    public String encode(String password) {
        return passwordEncoder.encode(password);
    }
}
