package com.javanauta.user.infrastructure.repository;

import com.javanauta.user.infrastructure.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<User, Long> {
}