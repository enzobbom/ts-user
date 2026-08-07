package com.javanauta.ts.user.infrastructure.client.cep.mapper;

import com.javanauta.ts.user.application.data.AddressCepLookupData;
import com.javanauta.ts.user.infrastructure.client.cep.dto.ExternalCepDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExternalCepMapper {
    @Mapping(source = "logradouro", target = "street")
    @Mapping(source = "localidade", target = "city")
    @Mapping(source = "bairro", target = "neighbourhood")
    @Mapping(source = "estado", target = "state")
    @Mapping(source = "cep", target = "cep")
    AddressCepLookupData toAddressData(ExternalCepDTO externalCepDTO);
}
