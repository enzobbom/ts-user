package com.javanauta.ts.user.domain.model;

import com.javanauta.ts.user.application.data.AddressData;
import com.javanauta.ts.user.application.data.PhoneData;
import com.javanauta.ts.user.application.data.CreateUserData;
import com.javanauta.ts.user.application.data.UpdateUserData;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_email", columnNames = "email")
        }
)
public class User implements UserDetails {
    @Id
    private UUID id;

    @Column(name = "name", length = 150)
    private String name;

    @Column(name = "email", length = 255, nullable = false)
    private String email;

    @Column(name = "password", length = 255)
    private String password;

    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Address address;

    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Phone phone;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return email;
    }

    private User(String name, String email, String password) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static User create(CreateUserData createUserData) {
        User user = new User(
                createUserData.name(),
                createUserData.email(),
                createUserData.password()
        );

        user.assignAddress(new Address(createUserData.addressData()));
        user.assignPhone(new Phone(createUserData.phoneData()));

        return user;
    }

    private void assignAddress(Address address) {
        this.address = address;
        address.setUser(this);
    }

    private void assignPhone(Phone phone) {
        this.phone = phone;
        phone.setUser(this);
    }

    public void update(UpdateUserData updateUserData) {
        if (updateUserData.name() != null) { name = updateUserData.name(); }
        if (updateUserData.email() != null) { email = updateUserData.email(); }
        if (updateUserData.password() != null) { password = updateUserData.password(); }
    }

    public void updateAddress(AddressData addressData) {
        address.update(addressData);
    }

    public void updatePhone(PhoneData phoneData) {
        phone.update(phoneData);
    }
}
