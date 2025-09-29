package com.spring.hrm.feign_clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "attendance-service")
public interface AttendanceClient {

    @DeleteMapping("/api/attendances/staff/{staffId}")
    void deleteAttendanceByStaff(@PathVariable("staffId") int staffId);
}
