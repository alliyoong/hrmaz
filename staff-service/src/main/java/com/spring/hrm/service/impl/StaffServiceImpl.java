package com.spring.hrm.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.spring.hrm.entity.JobPosition;
import com.spring.hrm.entity.Staff;
import com.spring.hrm.entity.Staff.Gender;
import com.spring.hrm.entity.Staff.StaffStatus;
import com.spring.hrm.entity.dto.SearchStaffDto;
import com.spring.hrm.entity.dto.StaffAddRequestDto;
import com.spring.hrm.entity.dto.StaffDetailDto;
import com.spring.hrm.entity.mapper.StaffMapper;
import com.spring.hrm.feign_clients.AttendanceClient;
import com.spring.hrm.repository.JobPositionRepository;
import com.spring.hrm.repository.StaffRepository;
import com.spring.hrm.repository.StaffSpecifications;
import com.spring.hrm.service.StaffService;
import com.spring.hrm.utilities.PageResponseDto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {
    private final StaffRepository staffRepository;
    private final StaffMapper staffMapper;
    private final JobPositionRepository jobPositionRepository;
    private final AttendanceClient attendanceClient;

    @Override
    public List<Staff> getStaffList() {
        return staffRepository.findAll();
    }

    @Override
    public PageResponseDto<StaffDetailDto> getPage(SearchStaffDto term) {
        Pageable sortedPageable = PageRequest.of(term.pageNumber(), term.pageSize(), Sort.by("id").descending());
        log.info("Search dto: {} - {}", term.department(), term.status());

        Specification<Staff> spec = StaffSpecifications.byEmail(term.email())
                .and(StaffSpecifications.byName(term.name()))
                .and(StaffSpecifications.bySsn(term.ssn()))
                .and(StaffSpecifications.byDepartment(term.department()))
                .and(StaffSpecifications.byStatus(term.status()));
        // if (term != null) {
        // spec = StaffSpecifications.hasAccountByName(term.name());
        // } else {
        // spec = StaffSpecifications.hasAccount();
        // }
        Page<Staff> staffPage = staffRepository.findAll(spec, sortedPageable);
        var staffDtos = staffPage.getContent().stream()
                .map(staffMapper::toStaffDetailDto)
                .toList();
        return new PageResponseDto<StaffDetailDto>(
                staffDtos,
                staffPage.getNumber(),
                staffPage.getSize(),
                staffPage.getTotalElements(),
                staffPage.getTotalPages(),
                staffPage.isLast());
    }

    @Override
    public void addStaff(StaffAddRequestDto dto) {
        Staff toAdd = staffMapper.fromAddRequestDto(dto);
        log.info("Adding dto: {}", dto);
        log.info("Adding staff: {}", toAdd);
        staffRepository.save(toAdd);
    }

    @Transactional
    @Override
    public void deleteStaff(int id) {
        Staff toDelete = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException());

        // delete all attendance records associated with the staff
        attendanceClient.deleteAttendanceByStaff(id);

        staffRepository.delete(toDelete);
    }

    @Override
    public void editStaff(int id, StaffAddRequestDto dto) {
        Staff toEdit = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException());

        staffMapper.updateStaffFromDto(dto, toEdit);
        log.info("Updating dto: {}", dto);
        log.info("Updating staff: {}", toEdit);
        staffRepository.save(toEdit);
    }

    @Override
    public List<Staff> searchStaff(SearchStaffDto phrase) {
        // return staffRepository.findByNameContainingIgnoreCase(phrase);
        return List.of();
    }

    @Override
    public StaffDetailDto getDetail(int id) {
        // // Optional<Staff> toDetail = Optional.ofNullable(staffList.stream()
        // // .filter(s -> s.getId()==id)
        // // .findFirst()
        // // .orElseThrow(() -> new GeneralException(APP_404_STAFF)));
        // // Department department = deptList.stream()
        // // .filter(s -> s.getDepartmentId() == toDetail.get().getDepartmentId())
        // // .findFirst().get();
        // // if (toDetail.get().getType().equals(StaffType.INTERN)){
        // // var result = (InternStaff) toDetail.get();
        // // return new StaffDetailDto(result.getId(),
        // // result.getName(),
        // // result.getAge(),
        // // result.getGender(),
        // // result.getType(),
        // // department,
        // // result.getInternDuration(),
        // // BigDecimal.valueOf(0));
        // // }
        // // else if (toDetail.get().getType().equals(StaffType.FULLTIME)){
        // // var result = (FulltimeStaff) toDetail.get();
        // // return new StaffDetailDto(result.getId(),
        // // result.getName(),
        // // result.getAge(),
        // // result.getGender(),
        // // result.getType(),
        // // department,
        // // 0,
        // result.getSalary());
        // }
        throw new RuntimeException();
    }

    // bulk add data for testing purpose right now
    // @Transactional
    // @Override
    // public void saveList(List<StaffAddRequestDto> dtoList) {
    //     staffRepository.saveAll(dtoList.stream()
    //             .map(staffMapper::toStaff)
    //             .toList());
    // }

    // @Override
    // public Staff findById(int id) {
    //     return staffRepository.findById(id)
    //             .orElseThrow(() -> new RuntimeException());
    // }

    @Override
    public List<StaffStatus> getStaffStatusList() {
        return Arrays.asList(StaffStatus.values());
    }

    @Override
    public List<Gender> getGenderList() {
        return Arrays.asList(Gender.values());
    }

    @Override
    public List<JobPosition> getJobPositionList() {
        return jobPositionRepository.findAll();
    }

    @Override
    public Staff saveToDb(Staff staff) {
        return staffRepository.save(staff);
    }

    // @Override
    // public AccountViewDto findAccountByStaffId(int id) {
    // return accountService.findAccountByStaffId(id);
    // }
}
