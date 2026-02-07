package com.javanauta.ts.user.business;

import com.javanauta.ts.user.business.converter.CepConverter;
import com.javanauta.ts.user.business.dto.CepDTO;
import com.javanauta.ts.user.infrastructure.client.ViaCepClient;
import com.javanauta.ts.user.infrastructure.client.dto.ViaCepResponseDTO;
import com.javanauta.ts.user.infrastructure.exception.IllegalArgumentException;
import com.javanauta.ts.user.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ViaCepService {

    private final ViaCepClient viaCepClient;
    private final CepConverter cepConverter;

    public CepDTO getCEPDetails(String cep) {
        if (!Pattern.matches("^(\\d{8}|\\d{5}-\\d{3})$", cep)) {
            throw new IllegalArgumentException("Invalid CEP format");
        }

        ViaCepResponseDTO viaCepResponseDTO = viaCepClient.getCEPDetails(cep.replace("-", ""));

        if (viaCepResponseDTO.getCep() == null) {
            throw new ResourceNotFoundException("CEP not found");
        }

        return cepConverter.toCepDTO(viaCepResponseDTO);
    }
}
