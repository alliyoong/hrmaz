package com.spring.hrm.feign_clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.spring.hrm.entity.dto.DepartmentDto;
import com.spring.hrm.utilities.HttpResponse;

@FeignClient(name = "department-service")
public interface DepartmentClient {
    @GetMapping("/api/department/{departmentId}")
    HttpResponse<DepartmentDto> findDepartmentById(@PathVariable int departmentId);
}
