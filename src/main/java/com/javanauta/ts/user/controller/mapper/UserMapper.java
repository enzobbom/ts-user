package com.javanauta.ts.user.controller.mapper;

import com.javanauta.ts.user.controller.dto.in.CreateUserAddressDTO;
import com.javanauta.ts.user.controller.dto.in.CreateUserPhoneDTO;
import com.javanauta.ts.user.controller.dto.in.CreateUserRequestDTO;
import com.javanauta.ts.user.controller.dto.out.AddressDTO;
import com.javanauta.ts.user.controller.dto.out.AddressResponseDTO;
import com.javanauta.ts.user.controller.dto.out.PhoneResponseDTO;
import com.javanauta.ts.user.controller.dto.out.UserResponseDTO;
import com.javanauta.ts.user.domain.data.AddressData;
import com.javanauta.ts.user.domain.data.CreateUserData;
import com.javanauta.ts.user.domain.data.PhoneData;
import com.javanauta.ts.user.domain.model.Address;
import com.javanauta.ts.user.domain.model.Phone;
import com.javanauta.ts.user.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    CreateUserData fromCreateUserRequestDTO(CreateUserRequestDTO createUserRequestDTO);
    AddressData fromCreateUserAddressDTO(CreateUserAddressDTO createUserAddressDTO);
    PhoneData fromCreateUserPhoneDTO(CreateUserPhoneDTO createUserPhoneDTO);

    UserResponseDTO toUserDTO(User user);
    AddressResponseDTO toAddressDTO(Address address);
    PhoneResponseDTO toPhoneDTO(Phone phone);
}
