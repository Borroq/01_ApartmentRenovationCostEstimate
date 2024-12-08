package com.example.ApartmentRenovationCostEstimate.security;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<GrantedAuthorityImpl, Long> {
    Optional<GrantedAuthorityImpl> findByAuthority(String authority);
}
