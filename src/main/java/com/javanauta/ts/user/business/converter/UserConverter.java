package com.javanauta.ts.user.business.converter;

import com.javanauta.ts.user.business.dto.AddressDTO;
import com.javanauta.ts.user.business.dto.PhoneDTO;
import com.javanauta.ts.user.business.dto.UserDTO;
import com.javanauta.ts.user.infrastructure.entity.Address;
import com.javanauta.ts.user.infrastructure.entity.Phone;
import com.javanauta.ts.user.infrastructure.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserConverter {

    // User to UserDTO

    public User toUser(UserDTO userDTO) {
        return User.builder()
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .addresses(toAddressList(userDTO.getAddresses()))
                .phones(toPhoneList(userDTO.getPhones()))
                .build();
    }

    public List<Address> toAddressList(List<AddressDTO> addressDTOS) {
        return addressDTOS.stream().map(this::toAddress).toList();
    }

    public Address toAddress(AddressDTO addressDTO) {
        return Address.builder()
                .street(addressDTO.getStreet())
                .number(addressDTO.getNumber())
                .complement(addressDTO.getComplement())
                .city(addressDTO.getCity())
                .state(addressDTO.getState())
                .zipCode(addressDTO.getZipCode())
                .build();
    }

    public Address toAddress(AddressDTO addressDTO, Long userId) {
        // meant to add a new address to an existing user
        return Address.builder()
                .street(addressDTO.getStreet())
                .number(addressDTO.getNumber())
                .complement(addressDTO.getComplement())
                .city(addressDTO.getCity())
                .state(addressDTO.getState())
                .zipCode(addressDTO.getZipCode())
                .userId(userId)
                .build();
    }

    public List<Phone> toPhoneList(List<PhoneDTO> phoneDTOS) {
        return phoneDTOS.stream().map(this::toPhone).toList();
    }

    public Phone toPhone(PhoneDTO phoneDTO) {
        return Phone.builder()
                .countryCode(phoneDTO.getCountryCode())
                .areaCode(phoneDTO.getAreaCode())
                .number(phoneDTO.getNumber())
                .build();
    }

    public Phone toPhone(PhoneDTO phoneDTO, Long userId) {
        // meant to add a new phone to an existing user
        return Phone.builder()
                .countryCode(phoneDTO.getCountryCode())
                .areaCode(phoneDTO.getAreaCode())
                .number(phoneDTO.getNumber())
                .userId(userId)
                .build();
    }

    // UserDTO to User

    public UserDTO toUserDTO(User user) {
        return UserDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .addresses(toAddressDTOList(user.getAddresses()))
                .phones(toPhoneDTOList(user.getPhones()))
                .build();
    }

    public List<AddressDTO> toAddressDTOList(List<Address> addresses) {
        return addresses.stream().map(this::toAddressDTO).toList();
    }

    public AddressDTO toAddressDTO(Address address) {
        return AddressDTO.builder()
                .id(address.getId())
                .street(address.getStreet())
                .number(address.getNumber())
                .complement(address.getComplement())
                .city(address.getCity())
                .state(address.getState())
                .zipCode(address.getZipCode())
                .build();
    }

    public List<PhoneDTO> toPhoneDTOList(List<Phone> phones) {
        return phones.stream().map(this::toPhoneDTO).toList();
    }

    public PhoneDTO toPhoneDTO(Phone phone) {
        return PhoneDTO.builder()
                .id(phone.getId())
                .countryCode(phone.getCountryCode())
                .areaCode(phone.getAreaCode())
                .number(phone.getNumber())
                .build();
    }

    public User updateUser(UserDTO userDTO, User user) {
        return User.builder()
                .id(user.getId())
                .name(userDTO.getName() != null ? userDTO.getName() : user.getName())
                .email(userDTO.getEmail() != null ? userDTO.getEmail() : user.getEmail())
                .password(userDTO.getPassword() != null ? userDTO.getPassword() : user.getPassword())
                .addresses(user.getAddresses())
                .phones(user.getPhones())
                .build();
    }

    public Address updateAddress(AddressDTO addressDTO, Address address) {
        return Address.builder()
                .id(address.getId())
                .street(addressDTO.getStreet() != null ? addressDTO.getStreet() : address.getStreet())
                .number(addressDTO.getNumber() != null ? addressDTO.getNumber() : address.getNumber())
                .complement(addressDTO.getComplement() != null ? addressDTO.getComplement() : address.getComplement())
                .city(addressDTO.getCity() != null ? addressDTO.getCity() : address.getCity())
                .state(addressDTO.getState() != null ? addressDTO.getState() : address.getState())
                .zipCode(addressDTO.getZipCode() != null ? addressDTO.getZipCode() : address.getZipCode())
                .userId(address.getUserId())
                .build();
    }

    public Phone updatePhone(PhoneDTO phoneDTO, Phone phone) {
        return Phone.builder()
                .id(phone.getId())
                .countryCode(phoneDTO.getCountryCode() != null ? phoneDTO.getCountryCode() : phone.getCountryCode())
                .areaCode(phoneDTO.getAreaCode() != null ? phoneDTO.getAreaCode() : phone.getAreaCode())
                .number(phoneDTO.getNumber() != null ? phoneDTO.getNumber() : phone.getNumber())
                .userId(phone.getUserId())
                .build();
    }
}
