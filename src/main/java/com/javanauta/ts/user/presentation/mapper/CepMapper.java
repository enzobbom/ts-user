package com.javanauta.ts.user.presentation.mapper;

import com.javanauta.ts.user.application.data.AddressCepLookupData;
import com.javanauta.ts.user.presentation.dto.out.AddressCepLookupResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CepMapper {
    AddressCepLookupResponseDTO toAddressDTO(AddressCepLookupData addressCepLookupData);
}
