package com.example.hr_visualization_be.service.detail;

import com.example.hr_visualization_be.dto.OvertimeViolationDetailDTO;
import com.example.hr_visualization_be.repository.detail.OvertimeViolationDaily12hDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OvertimeViolationDaily12hDetailService {

    @Autowired
    private OvertimeViolationDaily12hDetailRepository repository;

    public List<OvertimeViolationDetailDTO> getDetails(String monthInput, String dept) {
        return repository.getOvertime12HDetailByDeptAndMonth(monthInput, dept).stream()
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
