package com.javanauta.ts.user.application.ports.out.security;

public interface UserAuthenticator {
    String login(String email, String password);
}
