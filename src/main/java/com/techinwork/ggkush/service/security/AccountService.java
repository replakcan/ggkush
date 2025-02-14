package com.techinwork.ggkush.service.security;

import com.techinwork.ggkush.entity.security.Account;

import java.util.List;

public interface AccountService {

    List<Account> findAll();

    Account save(Account account);
}
