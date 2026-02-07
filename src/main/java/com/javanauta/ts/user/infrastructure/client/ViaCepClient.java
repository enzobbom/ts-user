package com.javanauta.ts.user.infrastructure.client;

import com.javanauta.ts.user.infrastructure.client.dto.ViaCepResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "via-cep", url = "${viacep.url}")
public interface ViaCepClient {

    @GetMapping("/ws/{cep}/json/")
    ViaCepResponseDTO getCEPDetails(@PathVariable("cep") String cep);
}
