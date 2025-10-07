package com.spring.hrm.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.spring.hrm.entity.Attendance;
import com.spring.hrm.entity.Attendance.AttendanceStatus;
import com.spring.hrm.entity.dto.AttendanceViewDto;
import com.spring.hrm.entity.mapper.AttendanceMapper;
import com.spring.hrm.repository.AttendanceRepository;
import com.spring.hrm.repository.AttendanceSpecifications;
import com.spring.hrm.service.AttendanceService;
import com.spring.hrm.utilities.PageResponseDto;
import com.spring.hrm.feign_clients.StaffClient;

import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final AttendanceMapper attendanceMapper;
    private final StaffClient StaffClient;

    // @Override
    // public List<Attendance> getList() {
    // return attendanceRepository.findAll();
    // }

    @Override
    public Attendance getAttendance(int staffId) {
        var today = LocalDate.now();
        var attendance = attendanceRepository.findByStaffIdAndWorkDate(staffId, today)
                .orElse(null);
        return attendance;
    }

    @Override
    public void checkIn(int staffId) {
        // todo: check if staff exists
        var isStaffExisted = (boolean) StaffClient.isStaffExisted(staffId).getData();
        if(isStaffExisted){
            throw new RuntimeException();
        }

        var dateNow = LocalDate.now();
        var timeNow = LocalTime.now();
        isPossibleCheckedIn(staffId, dateNow);

        Attendance toAdd = Attendance.builder()
        .staffId(staffId)
        .attendanceStatus(AttendanceStatus.REQUESTED)
        .workDate(dateNow)
        .checkInTime(timeNow)
        .build();
        attendanceRepository.save(toAdd);
    }

    @Transactional
    @Override
    public void checkOut(int staffId) {
        // var target = staffRepository.findById(staffId)
        //         .orElseThrow(() -> new GeneralException(APP_404_STAFF));

        var dateNow = LocalDate.now();
        var timeNow = LocalTime.now();
        var toEdit = isPossibleCheckedOut(staffId, dateNow);
        var seconds = Duration.between(toEdit.getCheckInTime(), timeNow).getSeconds();
        toEdit.setTotalHours(new BigDecimal(seconds).divide(new BigDecimal(3600), 2, RoundingMode.HALF_UP));

        toEdit.setCheckOutTime(timeNow);
    }

    private void isPossibleCheckedIn(int staffId, LocalDate date) {
        var attendance = attendanceRepository.findByStaffIdAndWorkDate(staffId, date);
        if (!attendance.isPresent()) {
            return;
        }
        throw new RuntimeException();
    }

    private Attendance isPossibleCheckedOut(int staffId, LocalDate date) {
        var attendance = attendanceRepository.findByStaffIdAndWorkDate(staffId, date)
                .orElseThrow(() -> new RuntimeException());
        if (null == attendance.getCheckOutTime()) {
            return attendance;
        }
        throw new RuntimeException();
    }

    @Override
    public PageResponseDto<AttendanceViewDto> getPageAttendance(int staffId, int pageNumber, int pageSize, String fromDateString, String toDateString) {
        PageRequest sortedPageable = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());
        LocalDate fromDate = LocalDate.now();
        LocalDate toDate = LocalDate.now();
        if(!StringUtils.isBlank(fromDateString)){
            fromDate = LocalDate.parse(fromDateString.trim());
        }
        if(!StringUtils.isBlank(toDateString)){
            toDate = LocalDate.parse(toDateString.trim());
        }

        Specification<Attendance> spec = AttendanceSpecifications.betweenWorkDate(fromDate, toDate);
        Page<Attendance> attendancePage = attendanceRepository.findAllByStaffId(staffId, sortedPageable, spec);
        var attendanceDtos = attendancePage.getContent().stream()
                .map(attendanceMapper::toViewDto)
                .toList();
        return new PageResponseDto<AttendanceViewDto>(
                attendanceDtos,
                attendancePage.getNumber(),
                attendancePage.getSize(),
                attendancePage.getTotalElements(),
                attendancePage.getTotalPages(),
                attendancePage.isLast());
    }

}
