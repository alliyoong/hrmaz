package com.spring.hrm.feign_clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.spring.hrm.entity.dto.AccountDto;
import com.spring.hrm.utilities.HttpResponse;

@FeignClient(name = "account-service")
public interface AccountClient {

    @GetMapping("/username/{username}")
    HttpResponse<AccountDto> findAccountByUsername(@PathVariable String username);
    
    @GetMapping("/{id}")
    HttpResponse<AccountDto> findById(@PathVariable int id);
}
