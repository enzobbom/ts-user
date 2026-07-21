package com.javanauta.ts.user.application.ports.out.persistence;

import com.javanauta.ts.user.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserPersister {
    User save(User user);
    void delete(User user);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID id);
    void deleteByEmail(String email);
    void deleteById(UUID id);
}
