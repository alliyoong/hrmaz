package com.spring.hrm.entity;

import java.util.Collection;
import java.util.List;

import com.spring.hrm.entity.dto.AccountDto;

public class UserPrincipal {
    private final AccountDto account;

    public UserPrincipal(AccountDto account) {
        this.account = account;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public String getPassword() {
        return this.account.password();
    }

    public String getUsername() {
        return this.account.username();
    }

    public boolean isEnabled() {
        return this.account.accountStatus() == "ENABLED" ? true : false;
    }
}
