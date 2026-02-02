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
    @Column(name = "state", length = 2)
    private String state;
    @Column(name = "zip_code", length = 8)
    private String zipCode;
    @Column(name = "user_id")
    private Long userId;
}
