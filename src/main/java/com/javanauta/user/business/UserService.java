package com.javanauta.user.business;

import com.javanauta.user.business.converter.UserConverter;
import com.javanauta.user.business.dto.UserDTO;
import com.javanauta.user.infrastructure.entity.User;
import com.javanauta.user.infrastructure.exceptions.ConflictException;
import com.javanauta.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;

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
}
