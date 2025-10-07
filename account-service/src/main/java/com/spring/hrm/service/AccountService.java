package com.spring.hrm.service;

import java.util.List;

import com.spring.hrm.entity.Account;
import com.spring.hrm.entity.Account.AccountStatus;
import com.spring.hrm.entity.dto.AccountViewDto;
import com.spring.hrm.entity.dto.RegisterRequestDto;

public interface AccountService {
    Account findByUsername(String username);
    Account findById(int accountId);
    void saveToDb(RegisterRequestDto account);
    Account findWithStaff(String username);
    void editAccount(int id, RegisterRequestDto data);
    List<AccountStatus> getAccountStatusList();
    AccountViewDto findAccountByStaffId(int id);

}
