package com.spring.hrm.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.hrm.entity.Account;
import com.spring.hrm.entity.Account.AccountStatus;
import com.spring.hrm.entity.dto.AccountViewDto;
import com.spring.hrm.entity.dto.RegisterRequestDto;
import com.spring.hrm.entity.mapper.AccountMapper;
import com.spring.hrm.repository.AccountRepository;
import com.spring.hrm.repository.AccountSpecification;
import com.spring.hrm.service.AccountService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {
    private final AccountRepository repository;
    private final AccountMapper accountMapper;
    private final PasswordEncoder encoder;

    @Override
    public Account findByUsername(String username) {
        return repository.findByUsername(username)
                .orElse(null);
    }

    @Override
    public void saveToDb(RegisterRequestDto account) {
        var toAdd = accountMapper.toAccount(account);
        toAdd.setPassword(encoder.encode(toAdd.getPassword()));
        toAdd.setCreatedAt(LocalDateTime.now());
        log.info("Account info: {}",toAdd.toString());
        repository.save(toAdd);
    }

    @Override
    public Account findWithStaff(String username) {
        return repository.findAll(AccountSpecification.hasStaff(username)).get(0);
    }
    
    @Override
    public void editAccount(int id, RegisterRequestDto data) {
        log.info("data: {}", data);
        //check if exists
        Account toEdit = repository.findById(id)
                .orElseThrow(() -> new RuntimeException());
        
        accountMapper.updateAccountFromDto(data, toEdit);
        if (data.password() != null && !data.password().isBlank()) {
            toEdit.setPassword(encoder.encode(data.password()));
        }
        log.info("Editing account {}", toEdit);
        
        repository.save(toEdit);
    }
    
    @Override
    public List<AccountStatus> getAccountStatusList() {
        return Arrays.asList(AccountStatus.values());
    }

    @Override
    public AccountViewDto findAccountByStaffId(int id) {
        var found = repository.findByStaffId(id).orElse(null);
        return accountMapper.fromAccount(found);
    }

    @Override
    public Account findById(int accountId) {
        return repository.findById(accountId).orElse(null);
    }

}
