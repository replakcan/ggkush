package com.techinwork.ggkush.repository.security;

import com.techinwork.ggkush.entity.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("SELECT r FROM Role r WHERE r.authority = :authority")
    Optional<Role> findByAuthority(String authority);
}
