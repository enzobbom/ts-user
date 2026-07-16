package com.javanauta.ts.user.infrastructure.client.cep;

import com.javanauta.ts.user.infrastructure.client.cep.dto.ExternalCepDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "via-cep", url = "${viacep.url}")
public interface FeignCepClient {

    @GetMapping("/ws/{cep}/json/")
    ExternalCepDTO getCEPDetails(@PathVariable("cep") String cep);
}
