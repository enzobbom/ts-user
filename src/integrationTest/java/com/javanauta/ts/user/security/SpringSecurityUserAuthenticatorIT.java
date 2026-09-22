package com.javanauta.ts.user.security;

import com.javanauta.ts.user.application.data.AuthenticationResult;
import com.javanauta.ts.user.application.data.LoginData;
import com.javanauta.ts.user.application.exception.enums.ServiceExceptionCode;
import com.javanauta.ts.user.application.ports.out.persistence.UserPersister;
import com.javanauta.ts.user.domain.model.User;
import com.javanauta.ts.user.infrastructure.security.authentication.SpringSecurityUserAuthenticator;
import com.javanauta.ts.user.infrastructure.security.authentication.UserDetailsServiceImpl;
import com.javanauta.ts.user.infrastructure.security.jwt.JwtUtil;
import com.javanauta.ts.user.shared.exception.ApplicationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(
        classes = {
                SpringSecurityUserAuthenticator.class,
                UserDetailsServiceImpl.class,
                JwtUtil.class,
                SpringSecurityUserAuthenticatorIT.TestSecurityConfig.class
        },
        properties = {
                "spring.application.name=ts-user-test",
                "ts.jwt.secret=test-jwt-secret-that-is-definitely-long-enough",
                "ts.jwt.expiration=3600000"
        }
)
class SpringSecurityUserAuthenticatorIT {
    private static final UUID USER_ID = UUID.randomUUID();
    private static final String USER_EMAIL = "user@example.com";
    private static final String RAW_PASSWORD = "plain-password";
    private static final String WRONG_PASSWORD = "wrong-password";

    @MockitoBean
    private UserPersister userPersister;

    @Autowired
    private SpringSecurityUserAuthenticator underTest;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void login_shouldAuthenticateUserAndReturnTokenWhenCredentialsAreValid() {
        User user = User.builder()
                .id(USER_ID)
                .email(USER_EMAIL)
                .password(passwordEncoder.encode(RAW_PASSWORD))
                .build();

        when(userPersister.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));

        AuthenticationResult result = underTest.login(new LoginData(USER_EMAIL, RAW_PASSWORD));

        assertThat(result.userId()).isEqualTo(USER_ID);
        assertThat(result.userEmail()).isEqualTo(USER_EMAIL);
        assertThat(result.token()).isNotBlank();

        verify(userPersister).findByEmail(USER_EMAIL);
    }

    @Test
    void login_shouldThrowInvalidCredentialsWhenPasswordIsIncorrect() {
        User user = User.builder()
                .id(USER_ID)
                .email(USER_EMAIL)
                .password(passwordEncoder.encode(RAW_PASSWORD))
                .build();

        when(userPersister.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));

        assertThatThrownBy(() ->
                underTest.login(new LoginData(USER_EMAIL, WRONG_PASSWORD))
        ).isInstanceOfSatisfying(
                ApplicationException.class,
                ex -> assertThat(ex.getCode())
                        .isEqualTo(ServiceExceptionCode.INVALID_CREDENTIALS)
        );

        verify(userPersister).findByEmail(USER_EMAIL);
    }

    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        AuthenticationManager authenticationManager(
                UserDetailsService userDetailsService,
                PasswordEncoder passwordEncoder
        ) {
            DaoAuthenticationProvider authenticationProvider =
                    new DaoAuthenticationProvider(userDetailsService);

            authenticationProvider.setPasswordEncoder(passwordEncoder);

            return new ProviderManager(
                    List.of(authenticationProvider)
            );
        }
    }
}
