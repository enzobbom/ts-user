package com.javanauta.ts.user.application.ports.out.security;

import com.javanauta.ts.user.application.data.AuthenticationResult;
import com.javanauta.ts.user.application.data.LoginData;

public interface UserAuthenticator {
    public AuthenticationResult login(LoginData loginData);
}
