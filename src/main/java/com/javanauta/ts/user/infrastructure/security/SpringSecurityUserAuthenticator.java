package com.javanauta.ts.user.infrastructure.security;

import com.javanauta.ts.user.application.data.AuthenticationResult;
import com.javanauta.ts.user.application.data.LoginData;
import com.javanauta.ts.user.application.ports.out.security.UserAuthenticator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringSecurityUserAuthenticator implements UserAuthenticator {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public AuthenticationResult login(LoginData loginData) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginData.email(),
                        loginData.password()));

        return new AuthenticationResult(
                "Bearer " + jwtUtil.generateToken(authentication.getName()));
    }
}
