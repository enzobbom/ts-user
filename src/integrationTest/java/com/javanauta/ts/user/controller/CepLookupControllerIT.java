package com.javanauta.ts.user.controller;

import com.javanauta.ts.apicontract.response.enums.ResponseStatus;
import com.javanauta.ts.user.application.CepService;
import com.javanauta.ts.user.application.data.AddressCepLookupData;
import com.javanauta.ts.user.application.exception.enums.ServiceExceptionCode;
import com.javanauta.ts.user.infrastructure.security.authentication.ForwardedIdentityFilter;
import com.javanauta.ts.user.infrastructure.security.config.SecurityConfig;
import com.javanauta.ts.user.infrastructure.security.exception.JwtAuthenticationEntryPoint;
import com.javanauta.ts.user.presentation.controller.CepLookupController;
import com.javanauta.ts.user.presentation.exception.GlobalExceptionHandler;
import com.javanauta.ts.user.presentation.exception.enums.PresentationExceptionCode;
import com.javanauta.ts.user.presentation.mapper.CepMapperImpl;
import com.javanauta.ts.user.presentation.path.ApiPaths;
import com.javanauta.ts.user.shared.exception.ApplicationException;
import org.junit.jupiter.api.Test;
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

import static org.mockito.Mockito.*;

@SpringBootTest(
        classes = {
                CepLookupController.class,
                CepMapperImpl.class,
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
class CepLookupControllerIT {
    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CepService cepService;

    @Test
    void findAddressByCep_shouldReturnAddressWithoutAuthentication() {
        AddressCepLookupData addressData = mock(AddressCepLookupData.class);

        when(cepService.getCepDetails("12345-678")).thenReturn(addressData);

        webTestClient.get()
                .uri(ApiPaths.CEP_V1 + "/12345-678")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.status").isEqualTo(ResponseStatus.SUCCESS.toString())
                .jsonPath("$.code").isEqualTo(200)
                .jsonPath("$.data").exists();

        verify(cepService).getCepDetails("12345-678");
    }

    @Test
    void findAddressByCep_shouldReturnNotFoundWhenCepDoesNotExist() {
        when(cepService.getCepDetails("12345-678"))
                .thenThrow(
                        new ApplicationException(
                                ServiceExceptionCode.INEXISTENT_CEP
                        )
                );

        webTestClient.get()
                .uri(ApiPaths.CEP_V1 + "/12345-678")
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.status").isEqualTo(ResponseStatus.ERROR.toString())
                .jsonPath("$.code").isEqualTo(404)
                .jsonPath("$.errorCode").isEqualTo(
                        ServiceExceptionCode.INEXISTENT_CEP.getIdentifier()
                );

        verify(cepService).getCepDetails("12345-678");
    }

    @Test
    void findAddressByCep_shouldReturnUnprocessableContentWhenCepIsInvalid() {
        webTestClient.get()
                .uri(ApiPaths.CEP_V1 + "/invalid-cep")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_CONTENT)
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.status").isEqualTo(ResponseStatus.ERROR.toString())
                .jsonPath("$.code").isEqualTo(422)
                .jsonPath("$.errorCode").isEqualTo(
                        PresentationExceptionCode.PARAM_OR_PATH_VAR_VIOLATION_ERROR.getIdentifier()
                )
                .jsonPath("$.validationErrors").isArray();

        verifyNoInteractions(cepService);
    }
}
