package com.javanauta.ts.user.controller.converter;

import com.javanauta.ts.user.controller.dto.out.CepDTO;
import com.javanauta.ts.user.infrastructure.client.cep.dto.ExternalCepDTO;
import org.springframework.stereotype.Component;

@Component
public class CepConverter {

    public CepDTO toCepDTO(ExternalCepDTO cepDTO) {
        return CepDTO.builder()
                .street(cepDTO.getLogradouro())
                .city(cepDTO.getLocalidade())
                .neighbourhood(cepDTO.getBairro())
                .state(cepDTO.getEstado())
                .cep(cepDTO.getCep())
                .build();
    }
}
