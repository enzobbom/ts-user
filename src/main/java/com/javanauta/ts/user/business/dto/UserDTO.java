package com.javanauta.ts.user.business.dto;

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
