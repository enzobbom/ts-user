package com.javanauta.ts.user.infrastructure.client.cep;

import com.javanauta.ts.user.application.data.AddressCepLookupData;
import com.javanauta.ts.user.application.ports.out.client.cep.ExternalCepProvider;
import com.javanauta.ts.user.infrastructure.client.cep.dto.ExternalCepDTO;
import com.javanauta.ts.user.infrastructure.client.cep.mapper.ExternalCepMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExternalCepProviderAdapter implements ExternalCepProvider {
    private final FeignCepClient feignCepClient;
    private final ExternalCepMapper externalCepMapper;

    @Override
    public AddressCepLookupData getCepDetails(String cep) {
        ExternalCepDTO externalCepDTO = feignCepClient.getCepDetails(cep.replace("-", ""));
        if (externalCepDTO.getCep() == null) {
            return null;
        }

        return externalCepMapper.toAddressData(externalCepDTO);
    }
}

