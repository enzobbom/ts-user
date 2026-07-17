package com.javanauta.ts.user.domain.model;

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
}
