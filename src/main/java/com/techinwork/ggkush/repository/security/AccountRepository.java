package com.techinwork.ggkush.repository.security;

import com.techinwork.ggkush.entity.security.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
