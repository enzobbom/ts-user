package com.javanauta.ts.user.infrastructure.persistence;

import com.javanauta.ts.user.domain.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}