package com.javanauta.ts.user.application;

import com.javanauta.ts.user.application.data.AddressCepLookupData;
import com.javanauta.ts.user.application.exception.enums.ServiceExceptionCode;
import com.javanauta.ts.user.application.ports.out.client.cep.ExternalCepProvider;
import com.javanauta.ts.user.shared.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CepService {
    private final ExternalCepProvider externalCepProvider;

    public AddressCepLookupData getCepDetails(String cep) {
        AddressCepLookupData addressData = externalCepProvider.getCepDetails(cep);
        if (addressData == null) { throw new ApplicationException(ServiceExceptionCode.INEXISTENT_CEP);}
        return addressData;
    }
}
