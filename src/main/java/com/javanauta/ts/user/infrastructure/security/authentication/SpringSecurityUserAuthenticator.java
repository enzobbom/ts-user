package com.javanauta.ts.user.infrastructure.security.authentication;

import com.javanauta.ts.user.application.data.AuthenticationResult;
import com.javanauta.ts.user.application.data.LoginData;
import com.javanauta.ts.user.application.exception.enums.ServiceExceptionCode;
import com.javanauta.ts.user.application.ports.out.security.UserAuthenticator;
import com.javanauta.ts.user.infrastructure.security.jwt.JwtUtil;
import com.javanauta.ts.user.shared.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    loginData.email(),
                                    loginData.password()
                            ));

            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

            String token = jwtUtil.generateToken(securityUser.getUser());

            return new AuthenticationResult(
                    securityUser.getId(),
                    securityUser.getUsername(),
                    token);

        } catch (BadCredentialsException e) {
            throw new ApplicationException(ServiceExceptionCode.INVALID_CREDENTIALS);
        }
    }
}
