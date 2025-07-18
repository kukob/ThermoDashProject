package com.schneider.App.repository;

import com.schneider.App.model.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    @EntityGraph(attributePaths = {"userRoles", "userRoles.role"})
    UserEntity findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    UserEntity findByEmail(String email);
}