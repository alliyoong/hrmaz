package com.spring.hrm.restcontroller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.hrm.entity.Staff;
import com.spring.hrm.entity.dto.SearchStaffDto;
import com.spring.hrm.entity.dto.StaffAddRequestDto;
import com.spring.hrm.service.StaffService;
import com.spring.hrm.utilities.HttpResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
// @RequestMapping("/api/staff")
@RequiredArgsConstructor
@Validated
public class StaffRestController {
    private final StaffService service;

    @GetMapping()
    public HttpResponse<List<Staff>> getStaffList() {
        List<Staff> data = service.getStaffList();
        return HttpResponse.ok(data);
    }
    
    @GetMapping("/me")
    public HttpResponse<Staff> getMyProfile(@RequestHeader("x-staff-id") String staffId) {
        log.info("Request from gateway with staffId: {}", staffId);
        return HttpResponse.noContent();
    }
    
    @GetMapping("/exist/department/{departmentId}")
    public HttpResponse isDepartmentEmpty(@PathVariable int departmentId) {
        var result = service.isDepartmentEmpty(departmentId);
        return HttpResponse.ok(result);
    }
    
    @GetMapping("/exist/attendance/{staffId}")
    public HttpResponse isStaffExisted(@PathVariable int staffId) {
        var result = service.isStaffExisted(staffId);
        return HttpResponse.ok(result);
    }

    @PostMapping("/search")
    public HttpResponse getPage(@RequestBody SearchStaffDto search) {
        log.info("search criteria: {}", search.toString());
        var result = service.getPage(search);
        return HttpResponse.ok(result);
    }

    @GetMapping("/detail/{id}")
    public HttpResponse getDetail(@PathVariable("id") int id) {
        var result = service.getDetail(id);
        return HttpResponse.ok(result);
    }

    @PostMapping()
    public HttpResponse add(@Validated @RequestBody StaffAddRequestDto data) {
        log.info("Adding staff: {}", data.toString());
        service.addStaff(data);
        return HttpResponse.created();
    }

    @PutMapping(path = "/{id}")
    public HttpResponse edit(@PathVariable("id") int id,
            @Validated @RequestBody StaffAddRequestDto data) {
        service.editStaff(id, data);
        return HttpResponse.created();
    }

    @DeleteMapping("/{id}")
    public HttpResponse deleteAccount(@PathVariable int id) {
        service.deleteStaff(id);
        return HttpResponse.noContent();
    }
    

    @GetMapping("/status")
    public HttpResponse getStatusList() {
        var result = service.getStaffStatusList();
        return HttpResponse.ok(result);
    }

    @GetMapping("/gender")
    public HttpResponse getGenderList() {
        var result = service.getGenderList();
        return HttpResponse.ok(result);
    }

    @GetMapping("/job-position")
    public HttpResponse getJobPositionList() {
        var result = service.getJobPositionList();
        return HttpResponse.ok(result);
    }
}
