package com.javanauta.ts.user.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "phone")
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "country_code", length = 2)
    private String countryCode;
    @Column(name = "area_code", length = 2)
    private String areaCode;
    @Column(name = "number", length = 9)
    private String number;
    @Column(name = "user_id")
    private Long userId;
}
