package com.spring.auth_service.repository;

import com.spring.auth_service.entity.AuthUser;
import com.spring.auth_service.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<AuthUser> findByUsername(String username);

    Optional<AuthUser> findByEmail(String email);

    boolean existsByRole(Role role);

    long countByIsActiveTrue();

    long countByEmailVerifiedTrue();

    long countByAccountNonLockedFalse();
}
