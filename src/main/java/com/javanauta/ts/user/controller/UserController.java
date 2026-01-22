package com.javanauta.ts.user.controller;

import com.javanauta.ts.user.business.UserService;
import com.javanauta.ts.user.business.dto.AddressDTO;
import com.javanauta.ts.user.business.dto.PhoneDTO;
import com.javanauta.ts.user.business.dto.UserDTO;
import com.javanauta.ts.user.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.saveUser(userDTO));
    }

    @PostMapping("/login")
    public String login(@RequestBody UserDTO userDTO) {
        return userService.login(userDTO);
    }

    @GetMapping
    public ResponseEntity<UserDTO> getUserByEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUserByEmail(@PathVariable String email) {
        userService.deleteUserByEmail(email);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(userService.updateUser(token, userDTO));
    }

    @PutMapping("/address")
    public ResponseEntity<AddressDTO> updateAddress(@RequestBody AddressDTO addressDTO, @RequestParam("id") Long id) {
        return ResponseEntity.ok(userService.updateAddress(id, addressDTO));
    }

    @PutMapping("/phone")
    public ResponseEntity<PhoneDTO> updatePhone(@RequestBody PhoneDTO phoneDTO, @RequestParam("id") Long id) {
        return ResponseEntity.ok(userService.updatePhone(id, phoneDTO));
    }

    @PostMapping("/address")
    public ResponseEntity<AddressDTO> addAddress(@RequestBody AddressDTO addressDTO, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(userService.addAddress(token, addressDTO));
    }

    @PostMapping("/phone")
    public ResponseEntity<PhoneDTO> addPhone(@RequestBody PhoneDTO phoneDTO, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(userService.addPhone(token, phoneDTO));
    }
}
