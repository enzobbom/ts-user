package com.javanauta.user.infrastructure.repository;

import com.javanauta.user.infrastructure.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email); // 'Optional' avoids the NullPointerException (in case the user doesn't exist in the database here)

    @Transactional // creates a database transaction to wrap the deletion process
    void deleteByEmail(String email);
}
