package com.spring.hrm.service;

import java.time.LocalDate;
import java.util.List;

import com.spring.hrm.entity.Attendance;
import com.spring.hrm.entity.dto.AttendanceViewDto;
import com.spring.hrm.utilities.PageResponseDto;

public interface AttendanceService {
    void checkIn(int staffId);
    void checkOut(int staffId);
    Attendance getAttendance(int staffId);
    PageResponseDto<AttendanceViewDto> getPageAttendance(int staffId, int pageNumber, int pageSize, String fromDate, String toDate);
}
