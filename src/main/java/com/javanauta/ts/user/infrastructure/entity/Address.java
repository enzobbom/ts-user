package com.javanauta.ts.user.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
    @Column(name = "user_id")
    private Long userId;
}
