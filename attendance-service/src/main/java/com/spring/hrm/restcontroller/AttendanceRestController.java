package com.spring.hrm.restcontroller;

import java.time.LocalDate;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.hrm.service.AttendanceService;
import com.spring.hrm.utilities.HttpResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
// @RequestMapping("/api/attendance")
@Validated
@Slf4j
public class AttendanceRestController {
     private final AttendanceService service;

    @GetMapping(path = "/{staffId}", params = { "page", "size", "fromDate", "toDate" })
    public HttpResponse getPage(
        @PathVariable("staffId") int staffId,
        @RequestParam("size") int size,
        @RequestParam("page") int page,
        @RequestParam("fromDate") String fromDate,
        @RequestParam("toDate") String toDate
        ) {
        log.info("Page attendance info: {} - {} - {} - {} - {}", staffId, page, size, fromDate, toDate);
        var result = service.getPageAttendance(staffId, page, size, fromDate, toDate);
        return HttpResponse.ok(result);
    }

    @GetMapping("/{id}")
    public HttpResponse getAttendance(@PathVariable("id") int id) {
        var result = service.getAttendance(id);
        return HttpResponse.ok(result);
    }

    @GetMapping("/check-in/{staffId}")
    public HttpResponse checkIn(@PathVariable("staffId") int staffId) {
        service.checkIn(staffId);
        return HttpResponse.created();
    }

    @GetMapping("/check-out/{staffId}")
    public HttpResponse checkOut(@PathVariable("staffId") int staffId) {
        service.checkOut(staffId);
        return HttpResponse.created();
    }

}
