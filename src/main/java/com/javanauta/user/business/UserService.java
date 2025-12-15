package com.javanauta.user.business;

import com.javanauta.user.business.converter.UserConverter;
import com.javanauta.user.business.dto.AddressDTO;
import com.javanauta.user.business.dto.PhoneDTO;
import com.javanauta.user.business.dto.UserDTO;
import com.javanauta.user.infrastructure.entity.Address;
import com.javanauta.user.infrastructure.entity.Phone;
import com.javanauta.user.infrastructure.entity.User;
import com.javanauta.user.infrastructure.exceptions.ConflictException;
import com.javanauta.user.infrastructure.exceptions.ResourceNotFoundException;
import com.javanauta.user.infrastructure.repository.AddressRepository;
import com.javanauta.user.infrastructure.repository.PhoneRepository;
import com.javanauta.user.infrastructure.repository.UserRepository;
import com.javanauta.user.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AddressRepository addressRepository;
    private final PhoneRepository phoneRepository;

    public UserDTO saveUser(UserDTO userDTO) {
        validateEmailNotExists(userDTO.getEmail());
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        User user = userConverter.toUser(userDTO);
        return userConverter.toUserDTO(userRepository.save(user));
    }

    public void validateEmailNotExists(String email) {
        if (emailExists(email)) {
            throw new ConflictException(String.format("Email '%s' already exists.", email));
        }
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Email '%s' not found.", email)));

        return userConverter.toUserDTO(user);
    }

    public void deleteUserByEmail(String email) {
        userRepository.deleteByEmail(email);
    }

    public UserDTO updateUser(String token, UserDTO userDTO) {
        String email = jwtUtil.extractUsername(token.substring(7));

        userDTO.setPassword(userDTO.getPassword() != null ? passwordEncoder.encode(userDTO.getPassword()) : null);
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Email '%s' not found.", email)));

        User updatedUser = userConverter.updateUser(userDTO, user);

        return userConverter.toUserDTO(userRepository.save(updatedUser));
    }

    public AddressDTO updateAddress(Long id, AddressDTO addressDTO) {
        Address address = addressRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Address with ID '%d' not found.", id)));

        Address updatedAddress = userConverter.updateAddress(addressDTO, address);

        return userConverter.toAddressDTO(addressRepository.save(updatedAddress));
    }

    public PhoneDTO updatePhone(Long id, PhoneDTO phoneDTO) {
        Phone phone = phoneRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Phone with ID '%d' not found.", id)));

        Phone updatedPhone = userConverter.updatePhone(phoneDTO, phone);

        return userConverter.toPhoneDTO(phoneRepository.save(updatedPhone));
    }
}
