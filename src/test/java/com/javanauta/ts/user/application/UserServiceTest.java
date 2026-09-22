package com.javanauta.ts.user.application;

import com.javanauta.ts.user.application.data.AddressData;
import com.javanauta.ts.user.application.data.CreateUserData;
import com.javanauta.ts.user.application.data.PhoneData;
import com.javanauta.ts.user.application.data.UpdateUserData;
import com.javanauta.ts.user.application.exception.enums.ServiceExceptionCode;
import com.javanauta.ts.user.application.ports.out.persistence.UserPersister;
import com.javanauta.ts.user.application.ports.out.security.PrincipalProvider;
import com.javanauta.ts.user.application.ports.out.security.UserPasswordEncoder;
import com.javanauta.ts.user.domain.model.Address;
import com.javanauta.ts.user.domain.model.Phone;
import com.javanauta.ts.user.domain.model.User;
import com.javanauta.ts.user.shared.exception.ApplicationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private static final UUID USER_ID = UUID.randomUUID();

    @Mock
    private UserPersister userPersister;

    @Mock
    private UserPasswordEncoder passwordEncoder;

    @Mock
    private PrincipalProvider principalProvider;

    @InjectMocks
    private UserService underTest;

    @Test
    void createUser_shouldCreateUserWithEncodedPasswordWhenEmailDoesNotExist() {
        CreateUserData userData = new CreateUserData(
                "Test User",
                "user@example.com",
                "plain-password",
                new AddressData(
                        "Street",
                        "123",
                        null,
                        "City",
                        "Neighbourhood",
                        "State",
                        "12345-678"
                ),
                new PhoneData(
                        "111",
                        "123456789"
                )
        );

        when(userPersister.existsByEmail("user@example.com")).thenReturn(false);
        when(passwordEncoder.encode("plain-password")).thenReturn("encoded-password");
        when(userPersister.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = underTest.createUser(userData);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userPersister).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertThat(result).isSameAs(savedUser);
        assertThat(savedUser.getName()).isEqualTo("Test User");
        assertThat(savedUser.getEmail()).isEqualTo("user@example.com");
        assertThat(savedUser.getPassword()).isEqualTo("encoded-password");

        verify(userPersister).existsByEmail("user@example.com");
        verify(passwordEncoder).encode("plain-password");
    }

    @Test
    void createUser_shouldThrowWhenEmailAlreadyExists() {
        CreateUserData userData = new CreateUserData(
                "Test User",
                "user@example.com",
                "plain-password",
                new AddressData(
                        "Street",
                        "123",
                        null,
                        "City",
                        null,
                        "State",
                        "12345-678"
                ),
                new PhoneData(
                        "111",
                        "123456789"
                )
        );

        when(userPersister.existsByEmail("user@example.com")).thenReturn(true);

        assertThatThrownBy(() -> underTest.createUser(userData))
                .isInstanceOfSatisfying(
                        ApplicationException.class,
                        ex -> assertThat(ex.getCode())
                                .isEqualTo(ServiceExceptionCode.USER_ALREADY_EXISTS)
                );

        verify(userPersister).existsByEmail("user@example.com");
        verifyNoInteractions(passwordEncoder);
        verify(userPersister, never()).save(any());
    }

    @Test
    void getUser_shouldReturnAuthenticatedUser() {
        User user = mock(User.class);

        when(principalProvider.getId()).thenReturn(USER_ID);
        when(userPersister.findById(USER_ID)).thenReturn(Optional.of(user));

        User result = underTest.getUser();

        assertThat(result).isSameAs(user);

        verify(principalProvider).getId();
        verify(userPersister).findById(USER_ID);
    }

    @Test
    void getUser_shouldThrowWhenUserDoesNotExist() {
        when(principalProvider.getId()).thenReturn(USER_ID);
        when(userPersister.findById(USER_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getUser())
                .isInstanceOfSatisfying(
                        ApplicationException.class,
                        ex -> assertThat(ex.getCode())
                                .isEqualTo(ServiceExceptionCode.USER_NOT_FOUND)
                );

        verify(principalProvider).getId();
        verify(userPersister).findById(USER_ID);
    }

    @Test
    void deleteUser_shouldDeleteAuthenticatedUser() {
        User user = mock(User.class);

        when(principalProvider.getId()).thenReturn(USER_ID);
        when(userPersister.findById(USER_ID)).thenReturn(Optional.of(user));

        underTest.deleteUser();

        verify(userPersister).delete(user);
    }

    @Test
    void updateUser_shouldEncodePasswordWhenPasswordIsProvided() {
        User user = mock(User.class);

        UpdateUserData updateData = new UpdateUserData(
                "Updated User",
                "updated@example.com",
                "new-password"
        );

        when(principalProvider.getId()).thenReturn(USER_ID);
        when(userPersister.findById(USER_ID)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("new-password")).thenReturn("encoded-password");

        User result = underTest.updateUser(updateData);

        ArgumentCaptor<UpdateUserData> updateCaptor = ArgumentCaptor.forClass(UpdateUserData.class);
        verify(user).update(updateCaptor.capture());

        UpdateUserData capturedData = updateCaptor.getValue();

        assertThat(result).isSameAs(user);
        assertThat(capturedData.name()).isEqualTo("Updated User");
        assertThat(capturedData.email()).isEqualTo("updated@example.com");
        assertThat(capturedData.password()).isEqualTo("encoded-password");

        verify(passwordEncoder).encode("new-password");
    }

    @Test
    void updateUser_shouldNotEncodePasswordWhenPasswordIsNotProvided() {
        User user = mock(User.class);

        UpdateUserData updateData = new UpdateUserData(
                "Updated User",
                null,
                null
        );

        when(principalProvider.getId()).thenReturn(USER_ID);
        when(userPersister.findById(USER_ID)).thenReturn(Optional.of(user));

        User result = underTest.updateUser(updateData);

        assertThat(result).isSameAs(user);

        verify(user).update(updateData);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void updateAddress_shouldUpdateAndReturnAddress() {
        User user = mock(User.class);
        Address address = mock(Address.class);

        AddressData addressData = new AddressData(
                "Updated Street",
                null,
                null,
                "Other City",
                null,
                null,
                null
        );

        when(principalProvider.getId()).thenReturn(USER_ID);
        when(userPersister.findById(USER_ID)).thenReturn(Optional.of(user));
        when(user.getAddress()).thenReturn(address);

        Address result = underTest.updateAddress(addressData);

        assertThat(result).isSameAs(address);

        verify(user).updateAddress(addressData);
    }

    @Test
    void updatePhone_shouldUpdateAndReturnPhone() {
        User user = mock(User.class);
        Phone phone = mock(Phone.class);

        PhoneData phoneData = new PhoneData(
                "222",
                null
        );

        when(principalProvider.getId()).thenReturn(USER_ID);
        when(userPersister.findById(USER_ID)).thenReturn(Optional.of(user));
        when(user.getPhone()).thenReturn(phone);

        Phone result = underTest.updatePhone(phoneData);

        assertThat(result).isSameAs(phone);

        verify(user).updatePhone(phoneData);
    }
}