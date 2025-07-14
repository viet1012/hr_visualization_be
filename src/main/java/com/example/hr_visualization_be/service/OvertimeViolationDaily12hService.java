package com.example.hr_visualization_be.service;

import com.example.hr_visualization_be.dto.OvertimeViolationSummaryDTO;
import com.example.hr_visualization_be.repository.OvertimeViolationDaily12hRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OvertimeViolationDaily12hService {

    @Autowired
    private OvertimeViolationDaily12hRepository repository;

    public List<OvertimeViolationSummaryDTO> getDaily12hOvertimeByMonth(String monthInput) {
        List<Object[]> results = repository.getOvertimeDaily12hByMonth(monthInput);
        List<OvertimeViolationSummaryDTO> dtos = new ArrayList<>();

        for (Object[] row : results) {
            String dept = (String) row[0];
            Integer tongLanVuot12Gio = ((Number) row[1]).intValue();
            LocalDate tuNgay = ((Date) row[2]).toLocalDate();
            LocalDate denNgay = ((Date) row[3]).toLocalDate();

            OvertimeViolationSummaryDTO dto = new OvertimeViolationSummaryDTO(dept, tongLanVuot12Gio, tuNgay, denNgay);
            dtos.add(dto);
        }

        return dtos;
    }
}
