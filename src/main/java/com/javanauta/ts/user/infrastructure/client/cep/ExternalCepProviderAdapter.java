package com.javanauta.ts.user.infrastructure.client.cep;

import com.javanauta.ts.user.application.ports.out.client.cep.ExternalCepProvider;
import com.javanauta.ts.user.domain.model.Address;
import com.javanauta.ts.user.infrastructure.client.cep.dto.ExternalCepDTO;
import com.javanauta.ts.user.infrastructure.client.cep.mapper.CepMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExternalCepProviderAdapter implements ExternalCepProvider {
    private final FeignCepClient cepProvider;
    private final CepMapper cepMapper;

    @Override
    public Address getCepDetails(String cep) {
        ExternalCepDTO externalDTO = cepProvider.getCepDetails(cep.replace("-", ""));
        if (externalDTO.getCep() == null) {
            return null;
        }

        return cepMapper.toAddress(externalDTO);
    }
}

