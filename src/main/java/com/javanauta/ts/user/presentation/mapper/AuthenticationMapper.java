package com.javanauta.ts.user.presentation.mapper;

import com.javanauta.ts.user.application.data.AuthenticationResult;
import com.javanauta.ts.user.application.data.LoginData;
import com.javanauta.ts.user.presentation.dto.in.LoginRequestDTO;
import com.javanauta.ts.user.presentation.dto.out.LoginResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AuthenticationMapper {
    LoginData toData(LoginRequestDTO loginRequestDTO);
    LoginResponseDTO toDTO(AuthenticationResult authenticationResult);
}
