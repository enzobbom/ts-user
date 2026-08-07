package com.javanauta.ts.user.application.ports.out.client.cep;

import com.javanauta.ts.user.application.data.AddressCepLookupData;

public interface ExternalCepProvider {
    AddressCepLookupData getCepDetails(String cep);
}
