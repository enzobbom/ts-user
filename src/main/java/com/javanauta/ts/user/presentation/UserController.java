package com.javanauta.ts.user.presentation;

import com.javanauta.ts.user.application.UserService;
import com.javanauta.ts.user.application.CepService;
import com.javanauta.ts.user.application.data.AuthenticationResult;
import com.javanauta.ts.user.domain.model.Address;
import com.javanauta.ts.user.domain.model.Phone;
import com.javanauta.ts.user.domain.model.User;
import com.javanauta.ts.user.infrastructure.security.config.SecurityConfig;
import com.javanauta.ts.user.presentation.dto.in.*;
import com.javanauta.ts.user.presentation.dto.out.*;
import com.javanauta.ts.user.presentation.mapper.AuthenticationMapper;
import com.javanauta.ts.user.presentation.mapper.UserMapper;
import com.javanauta.ts.user.shared.exception.IllegalArgumentException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "user", description = "Creation, login, update and deletion of Users")
@SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME)
public class UserController {
    private final UserMapper userMapper;
    private final AuthenticationMapper authenticationMapper;
    private final UserService userService;
    private final CepService cepService;

    @PostMapping
    @Operation(summary = "Create user", description = "Creates a new user")
    @ApiResponse(responseCode = "200", description = "User successfully created")
    @ApiResponse(responseCode = "409", description = "User already registered")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody CreateUserRequestDTO createUserRequestDTO) {
        User newUser = userService.createUser(userMapper.fromCreateUserRequestDTO(createUserRequestDTO));
        return ResponseEntity.ok(userMapper.toUserDTO(newUser));
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Logs an existing user in")
    @ApiResponse(responseCode = "200", description = "User successfully logged in")
    @ApiResponse(responseCode = "401", description = "Authentication error")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        AuthenticationResult authenticationResult = userService.login(authenticationMapper.toData(loginRequestDTO));
        return ResponseEntity.ok(authenticationMapper.toDTO(authenticationResult));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by email", description = "Gets the data of an user identified by their email")
    @ApiResponse(responseCode = "200", description = "User data successfully found")
    @ApiResponse(responseCode = "401", description = "Authentication error")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable UUID id) {
        User user = userService.getUser(id);
        return ResponseEntity.ok(userMapper.toUserDTO(user));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Deletes an user identified by their email")
    @ApiResponse(responseCode = "200", description = "User successfully deleted")
    @ApiResponse(responseCode = "401", description = "Authentication error")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    @Operation(summary = "Update user", description = "Updates an user identified by their email")
    @ApiResponse(responseCode = "200", description = "User successfully updated")
    @ApiResponse(responseCode = "401", description = "Authentication error")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<UserResponseDTO> updateUser(@RequestBody UpdateUserRequestDTO updateUserRequestDTO) {
        User updatedUser = userService.updateUser(userMapper.fromUpdateUserRequestDTO(updateUserRequestDTO));
        return ResponseEntity.ok(userMapper.toUserDTO(updatedUser));
    }

    @PutMapping("/address")
    @Operation(summary = "Update user address", description = "Updates an user's address identified by its ID")
    @ApiResponse(responseCode = "200", description = "User's address successfully updated")
    @ApiResponse(responseCode = "401", description = "Authentication error")
    @ApiResponse(responseCode = "404", description = "Address not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<AddressResponseDTO> updateAddress(@RequestBody UpdateUserAddressDTO updateUserAddressDTO) {
        Address updatedAddress = userService.updateAddress(userMapper.fromUpdateUserAddressDTO(updateUserAddressDTO));
        return ResponseEntity.ok(userMapper.toAddressDTO(updatedAddress));
    }

    @PutMapping("/phone")
    @Operation(summary = "Update user phone", description = "Updates an user's phone identified by its ID")
    @ApiResponse(responseCode = "200", description = "User's phone successfully updated")
    @ApiResponse(responseCode = "401", description = "Authentication error")
    @ApiResponse(responseCode = "404", description = "Phone not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<PhoneResponseDTO> updatePhone(@RequestBody UpdateUserPhoneDTO updateUserPhoneDTO) {
        Phone updatedPhone = userService.updatePhone(userMapper.fromUpdateUserPhoneDTO(updateUserPhoneDTO));
        return ResponseEntity.ok(userMapper.toPhoneDTO(updatedPhone));
    }

    @GetMapping("/address/{cep}")
    @Operation(summary = "Get CEP details", description = "Gets all details of a CEP")
    @ApiResponse(responseCode = "200", description = "CEP details successfully found")
    @ApiResponse(responseCode = "400", description = "CEP with invalid format")
    @ApiResponse(responseCode = "404", description = "CEP not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<AddressResponseDTO> findAddressByCep(@PathVariable("cep") String cep) {
        // To be moved to a validation annotation
        if (!Pattern.matches("^(\\d{8}|\\d{5}-\\d{3})$", cep)) {
            throw new IllegalArgumentException("Invalid CEP format");
        }

        Address foundAddress = cepService.getCepDetails(cep);
        return ResponseEntity.ok(userMapper.toAddressDTO(foundAddress));
    }
}
