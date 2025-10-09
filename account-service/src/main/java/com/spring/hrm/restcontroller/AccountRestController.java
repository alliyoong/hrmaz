package com.spring.hrm.restcontroller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.hrm.entity.dto.RegisterRequestDto;
import com.spring.hrm.service.AccountService;
import com.spring.hrm.utilities.HttpResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequiredArgsConstructor
@Slf4j
public class AccountRestController {
    private final AccountService service;

    @PostMapping()
    public HttpResponse register(@Validated @RequestBody RegisterRequestDto data) {
        log.info("Adding account: {}", data.toString());
        service.saveToDb(data);
        return HttpResponse.created();
    }

    @PutMapping(path = "/{id}")
    public HttpResponse edit(@PathVariable("id") int id,
            @Validated @RequestBody RegisterRequestDto data) {
        service.editAccount(id, data);
        return HttpResponse.created();
    }
    @GetMapping(path = "/{id}")
    public HttpResponse getById(@PathVariable int id) {
        var result = service.findById(id);
        return HttpResponse.ok(result);
    }

    @GetMapping("/status")
    public HttpResponse getStatusList() {
        var result = service.getAccountStatusList();
        return HttpResponse.ok(result);
    }
    
    @GetMapping("/username/{username}")
    public HttpResponse getByUsername(@PathVariable String username) {
        var result = service.findByUsername(username);
        return HttpResponse.ok(result);
    }

    @GetMapping("/check-has-account/{staffId}")
    public HttpResponse checkHasAccount(@PathVariable int staffId) {
        var result = service.findAccountByStaffId(staffId);
        return HttpResponse.ok(result);
    }
    
}
