package com.javanauta.ts.user.application;

import com.javanauta.ts.user.application.data.AddressCepLookupData;
import com.javanauta.ts.user.application.exception.enums.ServiceExceptionCode;
import com.javanauta.ts.user.application.ports.out.client.cep.ExternalCepProvider;
import com.javanauta.ts.user.shared.exception.ApplicationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CepServiceTest {
    @Mock
    private ExternalCepProvider externalCepProvider;

    @InjectMocks
    private CepService underTest;

    @Test
    void getCepDetails_shouldReturnAddressWhenCepExists() {
        AddressCepLookupData addressData = mock(AddressCepLookupData.class);

        when(externalCepProvider.getCepDetails("12345-678")).thenReturn(addressData);

        AddressCepLookupData result = underTest.getCepDetails("12345-678");

        assertThat(result).isSameAs(addressData);
        verify(externalCepProvider).getCepDetails("12345-678");
    }

    @Test
    void getCepDetails_shouldThrowWhenCepDoesNotExist() {
        when(externalCepProvider.getCepDetails("12345-678")).thenReturn(null);

        assertThatThrownBy(() -> underTest.getCepDetails("12345-678"))
                .isInstanceOfSatisfying(
                        ApplicationException.class,
                        ex -> assertThat(ex.getCode())
                                .isEqualTo(ServiceExceptionCode.INEXISTENT_CEP)
                );
    }
}