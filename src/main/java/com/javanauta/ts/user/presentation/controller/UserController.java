package com.javanauta.ts.user.presentation.controller;

import com.javanauta.ts.apicontract.response.ErrorResponse;
import com.javanauta.ts.apicontract.response.SuccessResponse;
import com.javanauta.ts.user.application.UserService;
import com.javanauta.ts.user.domain.model.Address;
import com.javanauta.ts.user.domain.model.Phone;
import com.javanauta.ts.user.domain.model.User;
import com.javanauta.ts.user.infrastructure.security.config.SecurityConfig;
import com.javanauta.ts.user.presentation.dto.in.CreateUserRequestDTO;
import com.javanauta.ts.user.presentation.dto.in.UpdateUserAddressDTO;
import com.javanauta.ts.user.presentation.dto.in.UpdateUserPhoneDTO;
import com.javanauta.ts.user.presentation.dto.in.UpdateUserRequestDTO;
import com.javanauta.ts.user.presentation.dto.out.AddressResponseDTO;
import com.javanauta.ts.user.presentation.dto.out.PhoneResponseDTO;
import com.javanauta.ts.user.presentation.dto.out.UserResponseDTO;
import com.javanauta.ts.user.presentation.mapper.UserMapper;
import com.javanauta.ts.user.presentation.path.ApiPaths;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPaths.USERS_V1)
@RequiredArgsConstructor
@Tag(name = "Users", description = "User registration, profile retrieval, updates and deletion")
@SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME)
@Validated
public class UserController {
    private final UserMapper userMapper;
    private final UserService userService;

    @PostMapping
    @Operation(summary = "Create user", description = "Creates a new user account")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User successfully created"),
            @ApiResponse(responseCode = "409", description = "User already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Request body validation failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<SuccessResponse<UserResponseDTO>> createUser(@Valid @RequestBody CreateUserRequestDTO createUserRequestDTO) {
        User createdUser = userService.createUser(userMapper.fromCreateUserRequestDTO(createUserRequestDTO));

        HttpStatus httpCode = HttpStatus.OK;
        SuccessResponse<UserResponseDTO> successResponse = new SuccessResponse<>(
                httpCode.value(),
                userMapper.toUserDTO(createdUser));

        return ResponseEntity.status(httpCode).body(successResponse);
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user", description = "Returns the authenticated user's profile information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User successfully found"),
            @ApiResponse(responseCode = "401", description = "Authentication failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<SuccessResponse<UserResponseDTO>> getUser() {
        User user = userService.getUser();

        HttpStatus httpCode = HttpStatus.OK;
        SuccessResponse<UserResponseDTO> successResponse = new SuccessResponse<>(
                httpCode.value(),
                userMapper.toUserDTO(user));

        return ResponseEntity.status(httpCode).body(successResponse);
    }

    @DeleteMapping("/me")
    @Operation(summary = "Delete current user", description = "Deletes the authenticated user's account")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User successfully deleted"),
            @ApiResponse(responseCode = "401", description = "Authentication failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> deleteUser() {
        userService.deleteUser();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/me")
    @Operation(summary = "Update current user", description = "Updates the authenticated user's profile information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User successfully updated"),
            @ApiResponse(responseCode = "401", description = "Authentication failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Request body validation failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<SuccessResponse<UserResponseDTO>> updateUser(@Valid @RequestBody UpdateUserRequestDTO updateUserRequestDTO) {
        User updatedUser = userService.updateUser(userMapper.fromUpdateUserRequestDTO(updateUserRequestDTO));

        HttpStatus httpCode = HttpStatus.OK;
        SuccessResponse<UserResponseDTO> successResponse = new SuccessResponse<>(
                httpCode.value(),
                userMapper.toUserDTO(updatedUser));

        return ResponseEntity.status(httpCode).body(successResponse);
    }

    @PatchMapping("/me/address")
    @Operation(
            summary = "Update current user's address",
            description = """
        Updates the authenticated user's address.

        PATCH behavior:
        - Missing fields are ignored.
        - Fields sent as null are ignored.
        - Send an empty string ("") to clear optional String fields ('complement', 'neighbourhood')
        """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Address successfully updated"),
            @ApiResponse(responseCode = "401", description = "Authentication failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Request body validation failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<SuccessResponse<AddressResponseDTO>> updateAddress(@Valid @RequestBody UpdateUserAddressDTO updateUserAddressDTO) {
        Address updatedAddress = userService.updateAddress(userMapper.fromUpdateUserAddressDTO(updateUserAddressDTO));

        HttpStatus httpCode = HttpStatus.OK;
        SuccessResponse<AddressResponseDTO> successResponse = new SuccessResponse<>(
                httpCode.value(),
                userMapper.toAddressDTO(updatedAddress));

        return ResponseEntity.status(httpCode).body(successResponse);
    }

    @PatchMapping("/me/phone")
    @Operation(summary = "Update current user's address", description = "Updates the authenticated user's phone")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Phone successfully updated"),
            @ApiResponse(responseCode = "401", description = "Authentication failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Request body validation failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<SuccessResponse<PhoneResponseDTO>> updatePhone(@Valid @RequestBody UpdateUserPhoneDTO updateUserPhoneDTO) {
        Phone updatedPhone = userService.updatePhone(userMapper.fromUpdateUserPhoneDTO(updateUserPhoneDTO));

        HttpStatus httpCode = HttpStatus.OK;
        SuccessResponse<PhoneResponseDTO> successResponse = new SuccessResponse<>(
                httpCode.value(),
                userMapper.toPhoneDTO(updatedPhone));

        return ResponseEntity.status(httpCode).body(successResponse);
    }
}
