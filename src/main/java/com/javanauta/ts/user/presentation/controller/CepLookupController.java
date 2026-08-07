package com.javanauta.ts.user.presentation.controller;

import com.javanauta.ts.apicontract.response.ErrorResponse;
import com.javanauta.ts.apicontract.response.SuccessResponse;
import com.javanauta.ts.user.application.CepService;
import com.javanauta.ts.user.application.data.AddressCepLookupData;
import com.javanauta.ts.user.infrastructure.security.config.SecurityConfig;
import com.javanauta.ts.user.presentation.dto.out.AddressCepLookupResponseDTO;
import com.javanauta.ts.user.presentation.mapper.CepMapper;
import com.javanauta.ts.user.presentation.path.ApiPaths;
import com.javanauta.ts.user.presentation.validation.Cep;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "CEP Lookup", description = "Brazilian CEP address lookup operations")
@SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME)
public class CepLookupController {
    private final CepService cepService;
    private final CepMapper cepMapper;

    @GetMapping("/{cep}")
    @Operation(summary = "Lookup CEP", description = "Retrieves address information associated with a Brazilian CEP")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "CEP details successfully found"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameter format",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "CEP not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "CEP validation failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<SuccessResponse<AddressCepLookupResponseDTO>> findAddressByCep(@NotBlank @Cep @PathVariable("cep") String cep) {
        AddressCepLookupData foundAddress = cepService.getCepDetails(cep);

        HttpStatus httpCode = HttpStatus.OK;
        SuccessResponse<AddressCepLookupResponseDTO> successResponse = new SuccessResponse<>(
                httpCode.value(),
                cepMapper.toAddressDTO(foundAddress));

        return ResponseEntity.status(httpCode).body(successResponse);
    }
}
