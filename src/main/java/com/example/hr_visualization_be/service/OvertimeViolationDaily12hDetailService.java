package com.example.hr_visualization_be.service;

import com.example.hr_visualization_be.dto.OvertimeViolationDetailDTO;
import com.example.hr_visualization_be.repository.OvertimeViolationDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OvertimeViolationDaily12hDetailService {

    @Autowired
    private  OvertimeViolationDetailRepository repository;

    public List<OvertimeViolationDetailDTO> getDetails(String monthInput, String dept) {
        return repository.getOvertimeDetailByDeptAndMonth(monthInput, dept).stream()
                .map(row -> new OvertimeViolationDetailDTO(
                        (String) row[0], // Dept
                        (String) row[1], // Group_Name
                        (String) row[2], // Ma_NV
                        (String) row[3], // Ten_NV
                        (row[4] != null) ? ((java.sql.Date) row[4]).toLocalDate() : null,
                        (Double) row[5], // Gio_WT
                        (Double) row[6], // Gio_OT
                        (Double) row[7]  // Tong_Gio
                ))
                .collect(Collectors.toList());
    }
}
