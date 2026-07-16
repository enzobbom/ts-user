package com.javanauta.ts.user.business.ports.out.security;

public interface UserAuthenticator {
    String login(String email, String password);
}
