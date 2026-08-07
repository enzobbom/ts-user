package com.javanauta.ts.user.infrastructure.persistence;

import com.javanauta.ts.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaUserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID id);

    @Transactional
    void deleteByEmail(String email);
    void deleteById(UUID id);
}
