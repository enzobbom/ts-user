package com.javanauta.ts.user.infrastructure.client.cep;

import com.javanauta.ts.user.application.data.AddressCepLookupData;
import com.javanauta.ts.user.infrastructure.client.cep.dto.ExternalCepDTO;
import com.javanauta.ts.user.infrastructure.client.cep.mapper.ExternalCepMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExternalCepProviderAdapterTest {

    @Mock
    private FeignCepClient feignCepClient;

    @Mock
    private ExternalCepMapper externalCepMapper;

    @InjectMocks
    private ExternalCepProviderAdapter underTest;

    @Test
    void getCepDetails_shouldRemoveHyphenBeforeCallingExternalProvider() {
        ExternalCepDTO externalCepDTO = new ExternalCepDTO();
        externalCepDTO.setCep("12345-678");

        AddressCepLookupData mappedData = mock(AddressCepLookupData.class);

        when(feignCepClient.getCepDetails("12345678")).thenReturn(externalCepDTO);
        when(externalCepMapper.toAddressData(externalCepDTO)).thenReturn(mappedData);

        AddressCepLookupData result = underTest.getCepDetails("12345-678");

        assertThat(result).isSameAs(mappedData);

        verify(feignCepClient).getCepDetails("12345678");
        verify(externalCepMapper).toAddressData(externalCepDTO);
    }

    @Test
    void getCepDetails_shouldReturnNullWhenExternalProviderDoesNotFindCep() {
        ExternalCepDTO externalCepDTO = new ExternalCepDTO();

        when(feignCepClient.getCepDetails("12345678")).thenReturn(externalCepDTO);

        AddressCepLookupData result = underTest.getCepDetails("12345678");

        assertThat(result).isNull();

        verify(feignCepClient).getCepDetails("12345678");
        verifyNoInteractions(externalCepMapper);
    }

    @Test
    void getCepDetails_shouldMapAndReturnExternalProviderResponse() {
        ExternalCepDTO externalCepDTO = new ExternalCepDTO();
        externalCepDTO.setCep("12345-678");

        AddressCepLookupData mappedData = mock(AddressCepLookupData.class);

        when(feignCepClient.getCepDetails("12345678")).thenReturn(externalCepDTO);
        when(externalCepMapper.toAddressData(externalCepDTO)).thenReturn(mappedData);

        AddressCepLookupData result = underTest.getCepDetails("12345678");

        assertThat(result).isSameAs(mappedData);

        verify(feignCepClient).getCepDetails("12345678");
        verify(externalCepMapper).toAddressData(externalCepDTO);
    }
}