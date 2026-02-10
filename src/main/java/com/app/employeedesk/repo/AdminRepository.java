package com.app.employeedesk.repo;

import com.app.employeedesk.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

public interface AdminRepository extends JpaRepository<Admin, UUID> {
    Optional<UserDetails> findByEmail(String username);
}
