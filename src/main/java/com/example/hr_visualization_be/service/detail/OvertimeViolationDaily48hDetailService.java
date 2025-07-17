package com.example.hr_visualization_be.service.detail;


import com.example.hr_visualization_be.dto.OvertimeViolation48HDetailDTO;
import com.example.hr_visualization_be.repository.detail.OvertimeViolationWeekly48hDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OvertimeViolationDaily48hDetailService {

    @Autowired
    private OvertimeViolationWeekly48hDetailRepository repository;

    public List<OvertimeViolation48HDetailDTO> getDetails(String monthInput, String dept) {
        return repository.getOvertime48HDetailByDeptAndMonth(monthInput, dept).stream()
                .map(row -> new OvertimeViolation48HDetailDTO(
                        (String) row[0], // Dept
                        (String) row[1], // Group_Name
                        (String) row[2], // Ma_NV
                        (String) row[3], // Ten_NV
                        ((java.sql.Date) row[4]).toLocalDate(), // Ten_NV
                        ((java.sql.Date) row[5]).toLocalDate(), // Ten_NV
                        (Double) row[6]// Gio_WT
                ))
                .collect(Collectors.toList());
    }
}
