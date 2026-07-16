package com.javanauta.ts.user.business.ports.out.security;

public interface UserPasswordEncoder {
    String encode(String password);
    String decode(String encodedPassword);
}
