package com.javanauta.ts.user.presentation.controller;

import com.javanauta.ts.apicontract.response.SuccessResponse;
import com.javanauta.ts.user.application.CepService;
import com.javanauta.ts.user.application.data.AddressCepLookupData;
import com.javanauta.ts.user.infrastructure.security.config.SecurityConfig;
import com.javanauta.ts.user.presentation.dto.out.AddressCepLookupResponseDTO;
import com.javanauta.ts.user.presentation.mapper.CepMapper;
import com.javanauta.ts.user.presentation.path.ApiPaths;
import com.javanauta.ts.user.presentation.validation.Cep;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<SuccessResponse> findAddressByCep(@NotBlank @Cep @PathVariable("cep") String cep) {
        AddressCepLookupData foundAddress = cepService.getCepDetails(cep);

        HttpStatus httpCode = HttpStatus.OK;
        SuccessResponse successResponse = new SuccessResponse(
                httpCode.value(),
                cepMapper.toAddressDTO(foundAddress));

        return ResponseEntity.status(httpCode).body(successResponse);
    }
}
