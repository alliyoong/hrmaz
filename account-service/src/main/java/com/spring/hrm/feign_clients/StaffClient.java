package com.spring.hrm.feign_clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.spring.hrm.entity.dto.StaffDto;
import com.spring.hrm.utilities.HttpResponse;

@FeignClient(name = "staff-service")
public interface StaffClient {
    
    @GetMapping("/{staffId}")
    HttpResponse<StaffDto> getStaffById(@PathVariable("staffId") int staffId);
}
