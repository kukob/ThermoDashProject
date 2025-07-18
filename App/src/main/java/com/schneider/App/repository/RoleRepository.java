package com.schneider.App.repository;

import com.schneider.App.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;



public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}