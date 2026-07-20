package com.javanauta.ts.user.application.ports.out.client.cep;

import com.javanauta.ts.user.domain.model.Address;

public interface ExternalCepProvider {
    Address getCepDetails(String cep);
}
