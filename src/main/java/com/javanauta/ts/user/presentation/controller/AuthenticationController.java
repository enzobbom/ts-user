package com.javanauta.ts.user.presentation.controller;

import com.javanauta.ts.apicontract.response.SuccessResponse;
import com.javanauta.ts.user.application.UserService;
import com.javanauta.ts.user.application.data.AuthenticationResult;
import com.javanauta.ts.user.infrastructure.security.config.SecurityConfig;
import com.javanauta.ts.user.presentation.dto.in.LoginRequestDTO;
import com.javanauta.ts.user.presentation.mapper.AuthenticationMapper;
import com.javanauta.ts.user.presentation.path.ApiPaths;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPaths.AUTH_V1)
@RequiredArgsConstructor
//@Tag(name = "user", description = "Creation, login, update and deletion of Users")
@SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME)
public class AuthenticationController {
    private final AuthenticationMapper authenticationMapper;
    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Logs an existing user in")
    @ApiResponse(responseCode = "200", description = "User successfully logged in")
    @ApiResponse(responseCode = "401", description = "Authentication error")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<SuccessResponse> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        AuthenticationResult authenticationResult = userService.login(authenticationMapper.toData(loginRequestDTO));

        HttpStatus httpCode = HttpStatus.OK;
        SuccessResponse successResponse = new SuccessResponse(
                httpCode.value(),
                authenticationMapper.toDTO(authenticationResult));

        return ResponseEntity.status(httpCode).body(successResponse);
    }
}
