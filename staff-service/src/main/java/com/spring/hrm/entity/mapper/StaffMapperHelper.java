package com.spring.hrm.entity.mapper;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.spring.hrm.entity.JobPosition;
import com.spring.hrm.entity.dto.DepartmentDto;
import com.spring.hrm.feign_clients.DepartmentClient;
import com.spring.hrm.repository.JobPositionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class StaffMapperHelper {
    private final DepartmentClient departmentClient;
    private final JobPositionRepository jobPositionRepository;

    public DepartmentDto resolveByDepartmentId(int id) {
        var result = (DepartmentDto) departmentClient.findDepartmentById(id).getData();
        if(Objects.isNull(result)){
            return new DepartmentDto(0, "", "");
        }
        return result;
    }
    
    public JobPosition resolveByJobPositionId(int id) {
        var result = jobPositionRepository.findById(id).orElse(null);
        return result;
    }
}
