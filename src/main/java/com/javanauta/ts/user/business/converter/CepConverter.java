package com.javanauta.ts.user.business.converter;

import com.javanauta.ts.user.business.dto.CepDTO;
import com.javanauta.ts.user.infrastructure.client.dto.ViaCepResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CepConverter {

    public CepDTO toCepDTO(ViaCepResponseDTO viaCepResponseDTO) {
        return CepDTO.builder()
                .street(viaCepResponseDTO.getLogradouro())
                .city(viaCepResponseDTO.getLocalidade())
                .neighbourhood(viaCepResponseDTO.getBairro())
                .state(viaCepResponseDTO.getEstado())
                .cep(viaCepResponseDTO.getCep())
                .build();
    }
}
