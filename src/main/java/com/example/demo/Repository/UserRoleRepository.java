package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

}
