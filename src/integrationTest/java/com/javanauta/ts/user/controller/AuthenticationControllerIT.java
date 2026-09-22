package com.javanauta.ts.user.controller;

import com.javanauta.ts.apicontract.response.enums.ResponseStatus;
import com.javanauta.ts.user.application.UserService;
import com.javanauta.ts.user.application.data.AuthenticationResult;
import com.javanauta.ts.user.application.data.LoginData;
import com.javanauta.ts.user.application.exception.enums.ServiceExceptionCode;
import com.javanauta.ts.user.infrastructure.security.authentication.ForwardedIdentityFilter;
import com.javanauta.ts.user.infrastructure.security.config.SecurityConfig;
import com.javanauta.ts.user.infrastructure.security.exception.JwtAuthenticationEntryPoint;
import com.javanauta.ts.user.presentation.controller.AuthenticationController;
import com.javanauta.ts.user.presentation.dto.in.LoginRequestDTO;
import com.javanauta.ts.user.presentation.exception.GlobalExceptionHandler;
import com.javanauta.ts.user.presentation.mapper.AuthenticationMapperImpl;
import com.javanauta.ts.user.presentation.path.ApiPaths;
import com.javanauta.ts.user.shared.exception.ApplicationException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(
        classes = {
                AuthenticationController.class,
                AuthenticationMapperImpl.class,
                SecurityConfig.class,
                ForwardedIdentityFilter.class,
                JwtAuthenticationEntryPoint.class,
                GlobalExceptionHandler.class
        },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureWebTestClient
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
class AuthenticationControllerIT {
    private static final UUID USER_ID = UUID.randomUUID();
    private static final String USER_EMAIL = "user@example.com";

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UserService userService;

    @Test
    void login_shouldAuthenticateUserWithoutExistingAuthentication() {
        LoginRequestDTO request = new LoginRequestDTO(
                USER_EMAIL,
                "password123"
        );

        when(userService.login(any(LoginData.class)))
                .thenReturn(new AuthenticationResult(
                        USER_ID,
                        USER_EMAIL,
                        "jwt-token"
                ));

        webTestClient.post()
                .uri(ApiPaths.AUTH_V1 + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.status").isEqualTo(ResponseStatus.SUCCESS.toString())
                .jsonPath("$.code").isEqualTo(200)
                .jsonPath("$.data.userId").isEqualTo(USER_ID.toString())
                .jsonPath("$.data.userEmail").isEqualTo(USER_EMAIL)
                .jsonPath("$.data.token").isEqualTo("Bearer jwt-token");

        ArgumentCaptor<LoginData> captor = ArgumentCaptor.forClass(LoginData.class);

        verify(userService).login(captor.capture());

        assertThat(captor.getValue().email()).isEqualTo(USER_EMAIL);
        assertThat(captor.getValue().password()).isEqualTo("password123");
    }

    @Test
    void login_shouldReturnUnauthorizedWhenCredentialsAreInvalid() {
        LoginRequestDTO request = new LoginRequestDTO(
                USER_EMAIL,
                "password123"
        );

        when(userService.login(any(LoginData.class)))
                .thenThrow(
                        new ApplicationException(
                                ServiceExceptionCode.INVALID_CREDENTIALS
                        )
                );

        webTestClient.post()
                .uri(ApiPaths.AUTH_V1 + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.status").isEqualTo(ResponseStatus.ERROR.toString())
                .jsonPath("$.code").isEqualTo(401)
                .jsonPath("$.errorCode").isEqualTo(
                        ServiceExceptionCode.INVALID_CREDENTIALS.getIdentifier()
                )
                .jsonPath("$.validationErrors").isArray();

        verify(userService).login(any(LoginData.class));
    }
}
