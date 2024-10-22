package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRoleName(String roleName);

    // Role findByRoleType(String role_type);
}
