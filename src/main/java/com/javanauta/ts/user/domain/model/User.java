package com.javanauta.ts.user.domain.model;

import com.javanauta.ts.user.domain.data.AddressData;
import com.javanauta.ts.user.domain.data.PhoneData;
import com.javanauta.ts.user.domain.data.UserData;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 150)
    private String name;

    @Column(name = "email", length = 255)
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
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static User create(UserData userData) {
        User user = new User(
                userData.name(),
                userData.email(),
                userData.password()
        );

        user.assignAddress(new Address(userData.addressData()));
        user.assignPhone(new Phone(userData.phoneData()));

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

    public void update(UserData userData) {
        if (userData.name() != null) { name = userData.name(); }
        if (userData.password() != null) { password = userData.password(); }
    }

    public void updateAddress(AddressData addressData) {
        address.update(addressData);
    }

    public void updatePhone(PhoneData phoneData) {
        phone.update(phoneData);
    }
}
