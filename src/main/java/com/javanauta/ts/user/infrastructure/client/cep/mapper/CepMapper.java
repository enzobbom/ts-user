package com.javanauta.ts.user.infrastructure.client.cep.mapper;

import com.javanauta.ts.user.application.data.AddressCepLookupData;
import com.javanauta.ts.user.infrastructure.client.cep.dto.ExternalCepDTO;
import com.javanauta.ts.user.presentation.dto.out.AddressCepLookupResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CepMapper {
    @Mapping(source = "logradouro", target = "street")
    @Mapping(source = "localidade", target = "city")
    @Mapping(source = "bairro", target = "neighbourhood")
    @Mapping(source = "estado", target = "state")
    @Mapping(source = "cep", target = "cep")
    AddressCepLookupData toAddressData(ExternalCepDTO externalCepDTO);
    AddressCepLookupResponseDTO toAddressDTO(AddressCepLookupData addressCepLookupData);
}
