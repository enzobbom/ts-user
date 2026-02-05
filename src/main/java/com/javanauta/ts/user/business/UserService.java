package com.javanauta.ts.user.business;

import com.javanauta.ts.user.business.converter.UserConverter;
import com.javanauta.ts.user.business.dto.AddressDTO;
import com.javanauta.ts.user.business.dto.PhoneDTO;
import com.javanauta.ts.user.business.dto.UserDTO;
import com.javanauta.ts.user.infrastructure.entity.Address;
import com.javanauta.ts.user.infrastructure.entity.Phone;
import com.javanauta.ts.user.infrastructure.entity.User;
import com.javanauta.ts.user.infrastructure.exception.ConflictException;
import com.javanauta.ts.user.infrastructure.exception.ResourceNotFoundException;
import com.javanauta.ts.user.infrastructure.repository.AddressRepository;
import com.javanauta.ts.user.infrastructure.repository.PhoneRepository;
import com.javanauta.ts.user.infrastructure.repository.UserRepository;
import com.javanauta.ts.user.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AddressRepository addressRepository;
    private final PhoneRepository phoneRepository;

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
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword()));
        return "Bearer " + jwtUtil.generateToken(authentication.getName());
    }

    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userConverter.toUserDTO(user);
    }

    public void deleteUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userRepository.deleteByEmail(email);
        log.info("User {} deleted", user.getId());
    }

    public UserDTO updateUser(String token, UserDTO userDTO) {
        String email = jwtUtil.extractUsername(token.substring(7));

        userDTO.setPassword(userDTO.getPassword() != null ? passwordEncoder.encode(userDTO.getPassword()) : null);

        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
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
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Address savedAddress = addressRepository.save(userConverter.toAddress(addressDTO, user.getId()));
        log.info("Address {} added to user {}", savedAddress.getId(), user.getId());

        return userConverter.toAddressDTO(savedAddress);
    }

    public PhoneDTO addPhone(String token, PhoneDTO phoneDTO) {
        String email = jwtUtil.extractUsername(token.substring(7));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Phone savedPhone = phoneRepository.save(userConverter.toPhone(phoneDTO, user.getId()));
        log.info("Phone {} added to user {}", savedPhone.getId(), user.getId());

        return userConverter.toPhoneDTO(savedPhone);
    }
}
