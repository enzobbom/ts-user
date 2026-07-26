package com.javanauta.ts.user.application.ports.out.security;

import java.util.UUID;

public interface PrincipalProvider {
    UUID getUserId();
    String getEmail();
}