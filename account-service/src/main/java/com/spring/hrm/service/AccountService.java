package com.spring.hrm.service;

import java.security.Key;
import java.util.List;

import com.spring.hrm.entity.Account;
import com.spring.hrm.entity.Account.AccountStatus;
import com.spring.hrm.entity.dto.KeyCloakUserDto;
import com.spring.hrm.entity.dto.RegisterRequestDto;

public interface AccountService {
    Account findByUsername(String username);
    Account findById(int accountId);
    void saveToDb(RegisterRequestDto account);
    Account findWithStaff(String username);
    void editAccount(String id, RegisterRequestDto data);
    List<AccountStatus> getAccountStatusList();
    KeyCloakUserDto findAccountByStaffId(int id);

}
