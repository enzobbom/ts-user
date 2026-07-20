package com.javanauta.ts.user.application;

import com.javanauta.ts.user.application.data.*;
import com.javanauta.ts.user.application.ports.out.persistence.UserPersister;
import com.javanauta.ts.user.application.ports.out.security.PrincipalProvider;
import com.javanauta.ts.user.application.ports.out.security.UserAuthenticator;
import com.javanauta.ts.user.application.ports.out.security.UserPasswordEncoder;
import com.javanauta.ts.user.domain.model.Address;
import com.javanauta.ts.user.domain.model.Phone;
import com.javanauta.ts.user.domain.model.User;
import com.javanauta.ts.user.shared.exception.ConflictException;
import com.javanauta.ts.user.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.sql.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserPersister userPersister;
    private final UserPasswordEncoder passwordEncoder;
    private final UserAuthenticator userAuthenticator;
    private final PrincipalProvider principalProvider;
    private static final String USER_NOT_FOUND_MSG = "User not found";

    @Transactional
    public User createUser(CreateUserData userData) {
        validateEmailNotExists(userData.email());

        User newUser = User.create(userData);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        User savedUser = userPersister.save(newUser);
        log.info("User {} created", savedUser.getId());

        return savedUser;
    }

    public AuthenticationResult login(LoginData loginData) {
        return userAuthenticator.login(loginData);
    }

    public User getUser(UUID id) {
        return userPersister.findById(id).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MSG));
    }

    public void deleteUser(UUID id) {
        User user = userPersister.findById(id).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MSG));

        userPersister.deleteById(id);
        log.info("User {} deleted", user.getId());
    }

    public User updateUser(UpdateUserData updateUserData) {
        String email = principalProvider.getEmail();
        User userToUpdate = userPersister.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MSG));

        if (updateUserData.password() != null) {
            updateUserData = new UpdateUserData(
                    updateUserData.name(),
                    updateUserData.email(),
                    passwordEncoder.encode(updateUserData.password())
            );
        }

        userToUpdate.update(updateUserData);

        log.info("User {} updated", userToUpdate.getId());

        return userToUpdate;
    }

    public Address updateAddress(AddressData addressData) {
        String userEmail = principalProvider.getEmail();

        User userToUpdateAddress = userPersister.findByEmail(userEmail).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MSG));
        userToUpdateAddress.updateAddress(addressData);

        log.info("Address of User {} was updated", userToUpdateAddress.getId());

        return userToUpdateAddress.getAddress();
    }

    public Phone updatePhone(PhoneData phoneData) {
        String userEmail = principalProvider.getEmail();

        User userToUpdatePhone = userPersister.findByEmail(userEmail).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MSG));
        userToUpdatePhone.updatePhone(phoneData);

        log.info("Phone of User {} was updated", userToUpdatePhone.getId());

        return userToUpdatePhone.getPhone();
    }

    // internal helper/validation methods

    private void validateEmailNotExists(String email) {
        if (emailExists(email)) {throw new ConflictException("Email already registered");}
    }

    private boolean emailExists(String email) {
        return userPersister.existsByEmail(email);
    }
}
