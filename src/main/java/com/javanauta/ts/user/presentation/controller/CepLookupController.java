package com.javanauta.ts.user.presentation.controller;

import com.javanauta.ts.user.application.CepService;
import com.javanauta.ts.user.application.data.AddressCepLookupData;
import com.javanauta.ts.user.infrastructure.security.config.SecurityConfig;
import com.javanauta.ts.user.presentation.dto.out.AddressCepLookupResponseDTO;
import com.javanauta.ts.user.presentation.mapper.CepMapper;
import com.javanauta.ts.user.presentation.path.ApiPaths;
import com.javanauta.ts.user.shared.exception.IllegalArgumentException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Pattern;

@RestController
@RequestMapping(ApiPaths.CEP_V1)
@RequiredArgsConstructor
//@Tag(name = "user", description = "Creation, login, update and deletion of Users")
@SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME)
public class CepLookupController {
    private final CepService cepService;
    private final CepMapper cepMapper;

    @GetMapping("/{cep}")
    @Operation(summary = "Get CEP details", description = "Gets all details of a CEP")
    @ApiResponse(responseCode = "200", description = "CEP details successfully found")
    @ApiResponse(responseCode = "400", description = "CEP with invalid format")
    @ApiResponse(responseCode = "404", description = "CEP not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<AddressCepLookupResponseDTO> findAddressByCep(@PathVariable("cep") String cep) {
        // To be moved to a validation annotation
        if (!Pattern.matches("^(\\d{8}|\\d{5}-\\d{3})$", cep)) {
            throw new IllegalArgumentException("Invalid CEP format");
        }

        AddressCepLookupData foundAddress = cepService.getCepDetails(cep);
        return ResponseEntity.ok(cepMapper.toAddressDTO(foundAddress));
    }
}
