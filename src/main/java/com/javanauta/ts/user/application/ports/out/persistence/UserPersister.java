package com.javanauta.ts.user.application.ports.out.persistence;

import com.javanauta.ts.user.domain.model.User;

import java.util.Optional;

public interface UserPersister {
    User save(User user);
    void delete(User user);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
