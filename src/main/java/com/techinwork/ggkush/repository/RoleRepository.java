package com.techinwork.ggkush.repository;

import com.techinwork.ggkush.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query(value = "SELECT * FROM ggkush.Role r WHERE r.authority = :authority", nativeQuery = true)
    Optional<Role> findByAuthority(String authority);
}
