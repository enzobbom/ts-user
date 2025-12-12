package com.javanauta.user.business.dto;

import jakarta.persistence.Column;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private String name;
    private String email;
    private String password;
    private List<AddressDTO> addresses;
    private List<PhoneDTO> phones;
}
