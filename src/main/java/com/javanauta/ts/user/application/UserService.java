package com.javanauta.ts.user.application;

import com.javanauta.ts.user.application.data.AuthenticationResult;
import com.javanauta.ts.user.application.data.LoginData;
import com.javanauta.ts.user.application.data.UpdateUserData;
import com.javanauta.ts.user.application.ports.out.security.PrincipalProvider;
import com.javanauta.ts.user.application.ports.out.security.UserAuthenticator;
import com.javanauta.ts.user.application.ports.out.security.UserPasswordEncoder;
import com.javanauta.ts.user.presentation.converter.UserConverter;
import com.javanauta.ts.user.presentation.dto.out.AddressDTO;
import com.javanauta.ts.user.presentation.dto.out.PhoneDTO;
import com.javanauta.ts.user.presentation.dto.out.UserDTO;
import com.javanauta.ts.user.application.data.CreateUserData;
import com.javanauta.ts.user.domain.model.Address;
import com.javanauta.ts.user.domain.model.Phone;
import com.javanauta.ts.user.domain.model.User;
import com.javanauta.ts.user.infrastructure.persistence.AddressRepository;
import com.javanauta.ts.user.infrastructure.persistence.PhoneRepository;
import com.javanauta.ts.user.infrastructure.persistence.UserRepository;
import com.javanauta.ts.user.infrastructure.security.JwtUtil;
import com.javanauta.ts.user.shared.exception.ConflictException;
import com.javanauta.ts.user.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final UserPasswordEncoder passwordEncoder;
    private final UserAuthenticator userAuthenticator;
    private final PrincipalProvider principalProvider;
    private final JwtUtil jwtUtil;
    private final AddressRepository addressRepository;
    private final PhoneRepository phoneRepository;
    private static final String USER_NOT_FOUND_MSG = "User not found";

    @Transactional
    public User createUser(CreateUserData userData) {
        validateEmailNotExists(userData.email());

        User newUser = User.create(userData);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        User savedUser = userRepository.save(newUser);
        log.info("User {} created", savedUser.getId());

        return savedUser;
    }

    public AuthenticationResult login(LoginData loginData) {
        return userAuthenticator.login(loginData);
    }

    public User getUser(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MSG));
    }

    public void deleteUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MSG));

        userRepository.deleteById(id);
        log.info("User {} deleted", user.getId());
    }

    public User updateUser(UpdateUserData updateUserData) {
        String email = principalProvider.getEmail();

        User userToUpdate = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MSG));
        userToUpdate.update(updateUserData);
        if (userToUpdate.getPassword() != null) {
            userToUpdate.setPassword(passwordEncoder.encode(userToUpdate.getPassword()));
        }

        log.info("User {} updated", userToUpdate.getId());

        return userToUpdate;
    }

    public AddressDTO updateAddress(Long id, AddressDTO addressDTO) {
        Address address = addressRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        Address updatedAddress = addressRepository.save(userConverter.updateAddress(addressDTO, address));
        log.info("Address {} updated", updatedAddress.getId());

        return userConverter.toAddressDTO(updatedAddress);
    }

    public PhoneDTO updatePhone(Long id, PhoneDTO phoneDTO) {
        Phone phone = phoneRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Phone not found"));

        Phone updatedPhone = phoneRepository.save(userConverter.updatePhone(phoneDTO, phone));
        log.info("Phone {} updated", updatedPhone.getId());

        return userConverter.toPhoneDTO(updatedPhone);
    }

    // internal helper/validation methods

    private void validateEmailNotExists(String email) {
        if (emailExists(email)) {throw new ConflictException("Email already registered");}
    }

    private boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
