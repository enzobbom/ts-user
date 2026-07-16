package com.javanauta.ts.user.application.ports.out.security;

public interface UserPasswordEncoder {
    public String encode(String password);
}
