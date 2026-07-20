package com.javanauta.ts.user.application;

import com.javanauta.ts.user.application.ports.out.client.cep.ExternalCepProvider;
import com.javanauta.ts.user.domain.model.Address;
import com.javanauta.ts.user.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CepService {
    private final ExternalCepProvider externalCepProvider;

    public Address getCepDetails(String cep) {
        Address address = externalCepProvider.getCepDetails(cep);
        if (address == null) { throw new ResourceNotFoundException("CEP not found"); }
        return address;
    }
}
