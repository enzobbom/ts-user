package com.javanauta.ts.user.domain.model;

import com.javanauta.ts.user.domain.data.AddressData;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
        name = "addresses",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "user_id")
        }
)
public class Address implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    @Column(name = "street", length = 150)
    private String street;

    @Column(name = "number")
    private Long number;

    @Column(name = "complement", length = 60)
    private String complement;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "neighbourhood", length = 60)
    private String neighbourhood;

    @Column(name = "state", length = 17) // length("Rio Grande do Sul") = 17
    private String state;

    @Column(name = "cep", length = 9)
    private String cep;

    public Address(AddressData addressData) {
        this.street = addressData.street();
        this.number = addressData.number();
        this.complement = addressData.complement();
        this.city = addressData.city();
        this.neighbourhood = addressData.neighbourhood();
        this.state = addressData.state();
        this.cep = addressData.cep();
    }

    public void update(AddressData addressData) {
        if (addressData.street() != null) { street = addressData.street(); }
        if (addressData.number() != null) { number = addressData.number(); }
        if (addressData.complement() != null) { complement = addressData.complement(); }
        if (addressData.city() != null) { city = addressData.city(); }
        if (addressData.neighbourhood() != null) { neighbourhood = addressData.neighbourhood(); }
        if (addressData.state() != null) { state = addressData.state(); }
        if (addressData.cep() != null) { cep = addressData.cep(); }
    }
}
