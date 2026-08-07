package com.javanauta.ts.user.application;

import com.javanauta.ts.user.application.data.*;
import com.javanauta.ts.user.application.exception.enums.ServiceExceptionCode;
import com.javanauta.ts.user.application.ports.out.persistence.UserPersister;
import com.javanauta.ts.user.application.ports.out.security.PrincipalProvider;
import com.javanauta.ts.user.application.ports.out.security.UserAuthenticator;
import com.javanauta.ts.user.application.ports.out.security.UserPasswordEncoder;
import com.javanauta.ts.user.domain.model.Address;
import com.javanauta.ts.user.domain.model.Phone;
import com.javanauta.ts.user.domain.model.User;
import com.javanauta.ts.user.shared.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Transactional
    public User createUser(CreateUserData userData) {
        validateEmailNotExists(userData.email());

        userData = new CreateUserData(
                userData.name(),
                userData.email(),
                passwordEncoder.encode(userData.password()),
                userData.addressData(),
                userData.phoneData()
        );

        User savedUser = userPersister.save(User.create(userData));
        log.info("User {} created", savedUser.getId());

        return savedUser;
    }

    public AuthenticationResult login(LoginData loginData) {
        return userAuthenticator.login(loginData);
    }

    public User getUser() {
        return getUserOrThrow(principalProvider.getId());
    }

    @Transactional
    public void deleteUser() {
        User user = getUserOrThrow(principalProvider.getId());
        userPersister.delete(user);

        log.info("User {} deleted", user.getId());
    }

    @Transactional
    public User updateUser(UpdateUserData updateUserData) {
        User userToUpdate = getUserOrThrow(principalProvider.getId());

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

    @Transactional
    public Address updateAddress(AddressData addressData) {
        User userToUpdateAddress = getUserOrThrow(principalProvider.getId());
        userToUpdateAddress.updateAddress(addressData);

        log.info("Address of User {} was updated", userToUpdateAddress.getId());

        return userToUpdateAddress.getAddress();
    }

    @Transactional
    public Phone updatePhone(PhoneData phoneData) {
        User userToUpdatePhone = getUserOrThrow(principalProvider.getId());
        userToUpdatePhone.updatePhone(phoneData);

        log.info("Phone of User {} was updated", userToUpdatePhone.getId());

        return userToUpdatePhone.getPhone();
    }

    // internal helper/validation methods

    private User getUserOrThrow(UUID id) {
        return userPersister.findById(id).orElseThrow(()
                -> new ApplicationException(ServiceExceptionCode.USER_NOT_FOUND));
    }

    private void validateEmailNotExists(String email) {
        if (userPersister.existsByEmail(email)) { throw new ApplicationException(ServiceExceptionCode.USER_ALREADY_EXISTS); }
    }
}
