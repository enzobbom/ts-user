package com.javanauta.ts.user.presentation.mapper;

import com.javanauta.ts.user.application.data.UpdateUserData;
import com.javanauta.ts.user.presentation.dto.in.*;
import com.javanauta.ts.user.presentation.dto.out.AddressResponseDTO;
import com.javanauta.ts.user.presentation.dto.out.PhoneResponseDTO;
import com.javanauta.ts.user.presentation.dto.out.UserResponseDTO;
import com.javanauta.ts.user.application.data.AddressData;
import com.javanauta.ts.user.application.data.CreateUserData;
import com.javanauta.ts.user.application.data.PhoneData;
import com.javanauta.ts.user.domain.model.Address;
import com.javanauta.ts.user.domain.model.Phone;
import com.javanauta.ts.user.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    CreateUserData fromCreateUserRequestDTO(CreateUserRequestDTO createUserRequestDTO);
    UpdateUserData fromUpdateUserRequestDTO(UpdateUserRequestDTO updateUserRequestDTO);
    AddressData fromCreateUserAddressDTO(CreateUserAddressDTO createUserAddressDTO);
    AddressData fromUpdateUserAddressDTO(UpdateUserAddressDTO updateUserAddressDTO);
    PhoneData fromCreateUserPhoneDTO(CreateUserPhoneDTO createUserPhoneDTO);
    PhoneData fromUpdateUserPhoneDTO(UpdateUserPhoneDTO updateUserPhoneDTO);

    UserResponseDTO toUserDTO(User user);
    AddressResponseDTO toAddressDTO(Address address);
    PhoneResponseDTO toPhoneDTO(Phone phone);
}
