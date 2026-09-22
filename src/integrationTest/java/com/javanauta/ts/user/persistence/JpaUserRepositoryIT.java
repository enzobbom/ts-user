package com.javanauta.ts.user.persistence;

import com.javanauta.ts.user.application.data.AddressData;
import com.javanauta.ts.user.application.data.CreateUserData;
import com.javanauta.ts.user.application.data.PhoneData;
import com.javanauta.ts.user.domain.model.Address;
import com.javanauta.ts.user.domain.model.Phone;
import com.javanauta.ts.user.domain.model.User;
import com.javanauta.ts.user.infrastructure.persistence.JpaUserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest(
        showSql = false,
        properties = {
                "spring.jpa.hibernate.ddl-auto=create"
        }
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class JpaUserRepositoryIT {
    @Container
    @ServiceConnection
    static final PostgreSQLContainer POSTGRES =
            new PostgreSQLContainer("postgres:14-alpine");

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void save_shouldPersistUserWithAddressAndPhone() {
        User user = createUser("user@example.com");

        userRepository.saveAndFlush(user);
        entityManager.clear();

        User persistedUser = userRepository.findById(user.getId()).orElseThrow();

        assertThat(persistedUser.getName()).isEqualTo("Test User");
        assertThat(persistedUser.getEmail()).isEqualTo("user@example.com");

        assertThat(persistedUser.getAddress()).isNotNull();
        assertThat(persistedUser.getAddress().getId()).isNotNull();
        assertThat(persistedUser.getAddress().getStreet()).isEqualTo("Street");
        assertThat(persistedUser.getAddress().getUser().getId()).isEqualTo(persistedUser.getId());

        assertThat(persistedUser.getPhone()).isNotNull();
        assertThat(persistedUser.getPhone().getId()).isNotNull();
        assertThat(persistedUser.getPhone().getCountryCode()).isEqualTo("111");
        assertThat(persistedUser.getPhone().getUser().getId()).isEqualTo(persistedUser.getId());
    }

    @Test
    void findByEmailAndExistsByEmail_shouldFindPersistedUser() {
        User user = createUser("user@example.com");

        userRepository.saveAndFlush(user);
        entityManager.clear();

        Optional<User> foundUser = userRepository.findByEmail("user@example.com");

        assertThat(foundUser)
                .isPresent()
                .get()
                .extracting(User::getId)
                .isEqualTo(user.getId());

        assertThat(userRepository.existsByEmail("user@example.com")).isTrue();
        assertThat(userRepository.existsByEmail("other@example.com")).isFalse();
    }

    @Test
    void save_shouldRejectDuplicateEmail() {
        User firstUser = createUser("user@example.com");
        User secondUser = createUser("user@example.com");

        userRepository.saveAndFlush(firstUser);

        assertThatThrownBy(() -> userRepository.saveAndFlush(secondUser))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void delete_shouldDeleteAssociatedAddressAndPhone() {
        User user = createUser("user@example.com");

        User savedUser = userRepository.saveAndFlush(user);

        Long addressId = savedUser.getAddress().getId();
        Long phoneId = savedUser.getPhone().getId();
        UUID userId = savedUser.getId();

        entityManager.clear();

        User persistedUser = userRepository.findById(userId).orElseThrow();

        userRepository.delete(persistedUser);
        userRepository.flush();
        entityManager.clear();

        assertThat(userRepository.findById(userId)).isEmpty();
        assertThat(entityManager.find(Address.class, addressId)).isNull();
        assertThat(entityManager.find(Phone.class, phoneId)).isNull();
    }

    private User createUser(String email) {
        return User.create(new CreateUserData(
                "Test User",
                email,
                "encoded-password",
                new AddressData(
                        "Street",
                        "123",
                        "Complement",
                        "City",
                        "Neighbourhood",
                        "State",
                        "12345-678"
                ),
                new PhoneData(
                        "111",
                        "123456789"
                )
        ));
    }
}
