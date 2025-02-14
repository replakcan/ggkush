package com.techinwork.ggkush.repository.security;

import com.techinwork.ggkush.entity.security.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT m FROM Member m WHERE m.email = :email") //JPQL
    Optional<Member> findByEmail(String email);

}
