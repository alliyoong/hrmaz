package com.spring.hrm.restcontroller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.hrm.entity.dto.DepartmentAddRequestDto;
import com.spring.hrm.service.DepartmentService;
import com.spring.hrm.utilities.HttpResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
// @RequestMapping("/api/department")
// @Validated
public class DepartmentRestController {
    private final DepartmentService service;

    @GetMapping()
    public HttpResponse getList() {
        var data = service.getDeptList();
        return HttpResponse.ok(data);
    }

    @GetMapping("/{id}")
    public HttpResponse findById(@PathVariable int id) {
        var data = service.findById(id);
        return HttpResponse.ok(data);
    }

    @GetMapping("/search")
    public HttpResponse search(@RequestParam(name = "name", required = false, defaultValue = "") String deptName) {
        var result = service.searchDepartment(deptName);
        return HttpResponse.ok(result);
    }

    @GetMapping("/detail/{id}")
    public HttpResponse getDetail(@PathVariable("id") int id) {
        var result = service.getDetail(id);
        return HttpResponse.ok(result);
    }

    @PostMapping()
    public HttpResponse add(@Validated() @RequestBody DepartmentAddRequestDto data) {
        service.addDepartment(data);
        return HttpResponse.created();
    }

    @PutMapping(path = "/{id}")
    public HttpResponse edit(@PathVariable("id") int id,
            @Validated() @RequestBody DepartmentAddRequestDto data) {
        log.info("Editing: {}", data.toString());
        service.editDepartment(id, data);
        return HttpResponse.created();
    }

    @DeleteMapping(path = "/{id}")
    public HttpResponse delete(@PathVariable int id) {
        service.deleteDepartment(id);
        return HttpResponse.noContent();
    }
}
