package com.javanauta.ts.user.controller;

import com.javanauta.ts.apicontract.http.HttpHeaders;
import com.javanauta.ts.apicontract.response.enums.ResponseStatus;
import com.javanauta.ts.user.application.UserService;
import com.javanauta.ts.user.application.data.AddressData;
import com.javanauta.ts.user.application.data.CreateUserData;
import com.javanauta.ts.user.application.data.PhoneData;
import com.javanauta.ts.user.application.data.UpdateUserData;
import com.javanauta.ts.user.application.exception.enums.ServiceExceptionCode;
import com.javanauta.ts.user.domain.model.User;
import com.javanauta.ts.user.infrastructure.security.authentication.ForwardedIdentityFilter;
import com.javanauta.ts.user.infrastructure.security.config.SecurityConfig;
import com.javanauta.ts.user.infrastructure.security.exception.JwtAuthenticationEntryPoint;
import com.javanauta.ts.user.infrastructure.security.exception.enums.SecurityExceptionCode;
import com.javanauta.ts.user.presentation.controller.UserController;
import com.javanauta.ts.user.presentation.dto.in.*;
import com.javanauta.ts.user.presentation.exception.GlobalExceptionHandler;
import com.javanauta.ts.user.presentation.exception.enums.PresentationExceptionCode;
import com.javanauta.ts.user.presentation.mapper.UserMapperImpl;
import com.javanauta.ts.user.presentation.path.ApiPaths;
import com.javanauta.ts.user.shared.exception.ApplicationException;
import com.javanauta.ts.user.shared.exception.enums.ValidationExceptionSourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(
        classes = {
                UserController.class,
                UserMapperImpl.class,
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
class UserControllerIT {
    private static final UUID USER_ID = UUID.randomUUID();
    private static final String USER_EMAIL = "user@example.com";

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.create(new CreateUserData(
                "Test User",
                USER_EMAIL,
                "encoded-password",
                new AddressData(
                        "Street",
                        "123",
                        "Complement",
                        "City",
                        "Neighbourhood",
                        "State",
                        "12345-678"
                ),
                new PhoneData(
                        "111",
                        "123456789"
                )
        ));
    }

    // Happy paths

    @Test
    void createUser_shouldCreateUserWithoutAuthentication() {
        CreateUserRequestDTO request = CreateUserRequestDTO.builder()
                .name("Test User")
                .email(USER_EMAIL)
                .password("password123")
                .address(CreateUserAddressDTO.builder()
                        .street("Street")
                        .number("123")
                        .complement("Complement")
                        .city("City")
                        .neighbourhood("Neighbourhood")
                        .state("State")
                        .cep("12345-678")
                        .build())
                .phone(CreateUserPhoneDTO.builder()
                        .countryCode("111")
                        .number("123456789")
                        .build())
                .build();

        when(userService.createUser(any(CreateUserData.class))).thenReturn(user);

        webTestClient.post()
                .uri(ApiPaths.USERS_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.status").isEqualTo(ResponseStatus.SUCCESS.toString())
                .jsonPath("$.code").isEqualTo(200)
                .jsonPath("$.data").exists();

        ArgumentCaptor<CreateUserData> captor = ArgumentCaptor.forClass(CreateUserData.class);

        verify(userService).createUser(captor.capture());

        CreateUserData mappedData = captor.getValue();

        assertThat(mappedData.name()).isEqualTo("Test User");
        assertThat(mappedData.email()).isEqualTo(USER_EMAIL);
        assertThat(mappedData.addressData().street()).isEqualTo("Street");
        assertThat(mappedData.phoneData().countryCode()).isEqualTo("111");
    }

    @Test
    void getUser_shouldReturnUserWhenAuthenticated() {
        when(userService.getUser()).thenReturn(user);

        webTestClient.get()
                .uri(ApiPaths.USERS_V1 + "/me")
                .header(HttpHeaders.USER_ID, USER_ID.toString())
                .header(HttpHeaders.USER_EMAIL, USER_EMAIL)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.status").isEqualTo(ResponseStatus.SUCCESS.toString())
                .jsonPath("$.code").isEqualTo(200)
                .jsonPath("$.data").exists();

        verify(userService).getUser();
    }

    @Test
    void deleteUser_shouldDeleteUserWhenAuthenticated() {
        webTestClient.delete()
                .uri(ApiPaths.USERS_V1 + "/me")
                .header(HttpHeaders.USER_ID, USER_ID.toString())
                .header(HttpHeaders.USER_EMAIL, USER_EMAIL)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();

        verify(userService).deleteUser();
    }

    @Test
    void updateUser_shouldUpdateUserWhenAuthenticated() {
        UpdateUserRequestDTO request = UpdateUserRequestDTO.builder()
                .name("Updated User")
                .build();

        when(userService.updateUser(any(UpdateUserData.class))).thenReturn(user);

        webTestClient.patch()
                .uri(ApiPaths.USERS_V1 + "/me")
                .header(HttpHeaders.USER_ID, USER_ID.toString())
                .header(HttpHeaders.USER_EMAIL, USER_EMAIL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.status").isEqualTo(ResponseStatus.SUCCESS.toString())
                .jsonPath("$.code").isEqualTo(200)
                .jsonPath("$.data").exists();

        verify(userService).updateUser(any(UpdateUserData.class));
    }

    @Test
    void updateAddress_shouldUpdateAddressWhenAuthenticated() {
        UpdateUserAddressDTO request = UpdateUserAddressDTO.builder()
                .city("Other City")
                .build();

        when(userService.updateAddress(any(AddressData.class))).thenReturn(user.getAddress());

        webTestClient.patch()
                .uri(ApiPaths.USERS_V1 + "/me/address")
                .header(HttpHeaders.USER_ID, USER_ID.toString())
                .header(HttpHeaders.USER_EMAIL, USER_EMAIL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.status").isEqualTo(ResponseStatus.SUCCESS.toString())
                .jsonPath("$.code").isEqualTo(200)
                .jsonPath("$.data").exists();

        verify(userService).updateAddress(any(AddressData.class));
    }

    @Test
    void updatePhone_shouldUpdatePhoneWhenAuthenticated() {
        UpdateUserPhoneDTO request = UpdateUserPhoneDTO.builder()
                .number("987654321")
                .build();

        when(userService.updatePhone(any(PhoneData.class))).thenReturn(user.getPhone());

        webTestClient.patch()
                .uri(ApiPaths.USERS_V1 + "/me/phone")
                .header(HttpHeaders.USER_ID, USER_ID.toString())
                .header(HttpHeaders.USER_EMAIL, USER_EMAIL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.status").isEqualTo(ResponseStatus.SUCCESS.toString())
                .jsonPath("$.code").isEqualTo(200)
                .jsonPath("$.data").exists();

        verify(userService).updatePhone(any(PhoneData.class));
    }

    // Security

    @Test
    void shouldReturnUnauthorizedWhenUserCannotBeAuthenticated() {
        webTestClient.get()
                .uri(ApiPaths.USERS_V1 + "/me")
                .exchange()
                .expectStatus().isUnauthorized()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.status").isEqualTo(ResponseStatus.ERROR.toString())
                .jsonPath("$.code").isEqualTo(401)
                .jsonPath("$.errorCode").isEqualTo(
                        SecurityExceptionCode.AUTHENTICATION_ERROR.getIdentifier()
                )
                .jsonPath("$.validationErrors").isArray();

        verifyNoInteractions(userService);
    }

    // Business errors

    @Test
    void createUser_shouldReturnConflictWhenUserAlreadyExists() {
        CreateUserRequestDTO request = validCreateUserRequest();

        when(userService.createUser(any(CreateUserData.class)))
                .thenThrow(
                        new ApplicationException(
                                ServiceExceptionCode.USER_ALREADY_EXISTS
                        )
                );

        webTestClient.post()
                .uri(ApiPaths.USERS_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.status").isEqualTo(ResponseStatus.ERROR.toString())
                .jsonPath("$.code").isEqualTo(409)
                .jsonPath("$.errorCode").isEqualTo(
                        ServiceExceptionCode.USER_ALREADY_EXISTS.getIdentifier()
                );

        verify(userService).createUser(any(CreateUserData.class));
    }

    @Test
    void getUser_shouldReturnNotFoundWhenUserDoesNotExist() {
        when(userService.getUser())
                .thenThrow(
                        new ApplicationException(
                                ServiceExceptionCode.USER_NOT_FOUND
                        )
                );

        webTestClient.get()
                .uri(ApiPaths.USERS_V1 + "/me")
                .header(HttpHeaders.USER_ID, USER_ID.toString())
                .header(HttpHeaders.USER_EMAIL, USER_EMAIL)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.status").isEqualTo(ResponseStatus.ERROR.toString())
                .jsonPath("$.code").isEqualTo(404)
                .jsonPath("$.errorCode").isEqualTo(
                        ServiceExceptionCode.USER_NOT_FOUND.getIdentifier()
                );

        verify(userService).getUser();
    }

    // Request validation

    @Test
    void updateUser_shouldReturnUnprocessableContentWhenRequestBodyHasNoFields() {
        UpdateUserRequestDTO request = UpdateUserRequestDTO.builder()
                .build();

        webTestClient.patch()
                .uri(ApiPaths.USERS_V1 + "/me")
                .header(HttpHeaders.USER_ID, USER_ID.toString())
                .header(HttpHeaders.USER_EMAIL, USER_EMAIL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_CONTENT)
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.status").isEqualTo(ResponseStatus.ERROR.toString())
                .jsonPath("$.code").isEqualTo(422)
                .jsonPath("$.errorCode").isEqualTo(
                        PresentationExceptionCode.REQUEST_BODY_VIOLATION_ERROR.getIdentifier()
                )
                .jsonPath("$.validationErrors").isArray()
                .jsonPath("$.validationErrors[0].sourceType")
                .isEqualTo(ValidationExceptionSourceType.OBJECT.getIdentifier());

        verifyNoInteractions(userService);
    }

    @Test
    void shouldReturnUnprocessableContentWhenRequestBodyFieldValidationFails() {
        CreateUserRequestDTO request = CreateUserRequestDTO.builder()
                .name("")
                .email("invalid-email")
                .password("short")
                .address(validAddress())
                .phone(validPhone())
                .build();

        webTestClient.post()
                .uri(ApiPaths.USERS_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_CONTENT)
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.status").isEqualTo(ResponseStatus.ERROR.toString())
                .jsonPath("$.code").isEqualTo(422)
                .jsonPath("$.errorCode").isEqualTo(
                        PresentationExceptionCode.REQUEST_BODY_VIOLATION_ERROR.getIdentifier()
                )
                .jsonPath("$.validationErrors").isArray();

        verifyNoInteractions(userService);
    }

    @Test
    void createUser_shouldReturnUnprocessableContentWhenNestedAddressValidationFails() {
        // This test specifically proves cascading validation into nested DTOs.
        // Everything at the parent level is valid; only address.street is invalid.

        CreateUserRequestDTO request = CreateUserRequestDTO.builder()
                .name("Test User")
                .email(USER_EMAIL)
                .password("password123")
                .address(CreateUserAddressDTO.builder()
                        .street("")
                        .number("123")
                        .city("City")
                        .state("State")
                        .cep("12345-678")
                        .build())
                .phone(validPhone())
                .build();

        webTestClient.post()
                .uri(ApiPaths.USERS_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_CONTENT)
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.status").isEqualTo(ResponseStatus.ERROR.toString())
                .jsonPath("$.code").isEqualTo(422)
                .jsonPath("$.errorCode").isEqualTo(
                        PresentationExceptionCode.REQUEST_BODY_VIOLATION_ERROR.getIdentifier()
                )
                .jsonPath("$.validationErrors").isArray();

        verifyNoInteractions(userService);
    }

    // Invalid request

    @Test
    void shouldReturnBadRequestWhenRequestBodyIsMalformed() {
        webTestClient.post()
                .uri(ApiPaths.USERS_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                            "name": "Test User",
                            "email": "user@example.com",
                            "password":
                        }
                        """)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.status").isEqualTo(ResponseStatus.ERROR.toString())
                .jsonPath("$.code").isEqualTo(400)
                .jsonPath("$.errorCode").isEqualTo(
                        PresentationExceptionCode.JSON_PARSE_ERROR.getIdentifier()
                )
                .jsonPath("$.validationErrors").isArray();

        verifyNoInteractions(userService);
    }

    private CreateUserRequestDTO validCreateUserRequest() {
        return CreateUserRequestDTO.builder()
                .name("Test User")
                .email(USER_EMAIL)
                .password("password123")
                .address(validAddress())
                .phone(validPhone())
                .build();
    }

    private CreateUserAddressDTO validAddress() {
        return CreateUserAddressDTO.builder()
                .street("Street")
                .number("123")
                .complement("Complement")
                .city("City")
                .neighbourhood("Neighbourhood")
                .state("State")
                .cep("12345-678")
                .build();
    }

    private CreateUserPhoneDTO validPhone() {
        return CreateUserPhoneDTO.builder()
                .countryCode("111")
                .number("123456789")
                .build();
    }
}
