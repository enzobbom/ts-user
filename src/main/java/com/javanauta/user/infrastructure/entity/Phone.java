package com.javanauta.user.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// some of the main Lombok funks to reduce boilerplate code
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// Spring data JPA
@Entity
@Table(name = "phone")
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "country_code", length = 3)
    private String countryCode;
    @Column(name = "area_code", length = 3)
    private String areaCode;
    @Column(name = "number", length = 10)
    private String number;
}
