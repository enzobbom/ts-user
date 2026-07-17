package com.javanauta.ts.user.application;

import com.javanauta.ts.user.controller.converter.CepConverter;
import com.javanauta.ts.user.controller.dto.out.CepDTO;
import com.javanauta.ts.user.infrastructure.client.cep.FeignCepClient;
import com.javanauta.ts.user.infrastructure.client.cep.dto.ExternalCepDTO;
import com.javanauta.ts.user.shared.exception.IllegalArgumentException;
import com.javanauta.ts.user.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ViaCepService {

    private final FeignCepClient viaCepClient;
    private final CepConverter cepConverter;

    public CepDTO getCEPDetails(String cep) {
        if (!Pattern.matches("^(\\d{8}|\\d{5}-\\d{3})$", cep)) {
            throw new IllegalArgumentException("Invalid CEP format");
        }

        ExternalCepDTO cepDTO = viaCepClient.getCEPDetails(cep.replace("-", ""));

        if (cepDTO.getCep() == null) {
            throw new ResourceNotFoundException("CEP not found");
        }

        return cepConverter.toCepDTO(cepDTO);
    }
}
