package com.javanauta.ts.user.application;

import com.javanauta.ts.user.application.ports.out.security.UserAuthenticator;
import com.javanauta.ts.user.application.ports.out.security.UserPasswordEncoder;
import com.javanauta.ts.user.controller.converter.UserConverter;
import com.javanauta.ts.user.controller.dto.in.AddressDTO;
import com.javanauta.ts.user.controller.dto.in.PhoneDTO;
import com.javanauta.ts.user.controller.dto.in.UserDTO;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final UserPasswordEncoder passwordEncoder;
    private final UserAuthenticator userAuthenticator;
    private final JwtUtil jwtUtil;
    private final AddressRepository addressRepository;
    private final PhoneRepository phoneRepository;
    private static final String USER_NOT_FOUND_MSG = "User not found";

    public UserDTO saveUser(UserDTO userDTO) {
        validateEmailNotExists(userDTO.getEmail());
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        User savedUser = userRepository.save(userConverter.toUser(userDTO));
        log.info("User {} created", savedUser.getId());

        return userConverter.toUserDTO(savedUser);
    }

    public void validateEmailNotExists(String email) {
        if (emailExists(email)) {throw new ConflictException("Email already registered");}
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public String login(UserDTO userDTO) {
        return userAuthenticator.login(userDTO.getEmail(), userDTO.getPassword());
    }

    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MSG));
        return userConverter.toUserDTO(user);
    }

    public void deleteUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MSG));

        userRepository.deleteByEmail(email);
        log.info("User {} deleted", user.getId());
    }

    public UserDTO updateUser(String token, UserDTO userDTO) {
        String email = jwtUtil.extractUsername(token.substring(7));

        userDTO.setPassword(userDTO.getPassword() != null ? passwordEncoder.encode(userDTO.getPassword()) : null);

        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MSG));
        User updatedUser = userRepository.save(userConverter.updateUser(userDTO, user));

        log.info("User {} updated", updatedUser.getId());

        return userConverter.toUserDTO(updatedUser);
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

    public AddressDTO addAddress(String token, AddressDTO addressDTO) {
        String email = jwtUtil.extractUsername(token.substring(7));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MSG));

        Address savedAddress = addressRepository.save(userConverter.toAddress(addressDTO, user.getId()));
        log.info("Address {} added to user {}", savedAddress.getId(), user.getId());

        return userConverter.toAddressDTO(savedAddress);
    }

    public PhoneDTO addPhone(String token, PhoneDTO phoneDTO) {
        String email = jwtUtil.extractUsername(token.substring(7));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MSG));

        Phone savedPhone = phoneRepository.save(userConverter.toPhone(phoneDTO, user.getId()));
        log.info("Phone {} added to user {}", savedPhone.getId(), user.getId());

        return userConverter.toPhoneDTO(savedPhone);
    }
}
