package com.javanauta.ts.user.domain.model;

import com.javanauta.ts.user.application.data.PhoneData;
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
        name = "phones",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "user_id")
        }
)
public class Phone implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    @Column(name = "country_code", length = 2)
    private String countryCode;

    @Column(name = "number", length = 9)
    private String number;

    public Phone(PhoneData phoneData){
        this.countryCode = phoneData.countryCode();
        this.number = phoneData.number();
    }

    public void update(PhoneData phoneData) {
        if (phoneData.countryCode() != null) { countryCode = phoneData.countryCode(); }
        if (phoneData.number() != null) { number = phoneData.number(); }
    }
}
