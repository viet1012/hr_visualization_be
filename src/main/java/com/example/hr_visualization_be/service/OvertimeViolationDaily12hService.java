package com.example.hr_visualization_be.service;

import com.example.hr_visualization_be.dto.OvertimeSummaryDTO;
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

    public List<OvertimeSummaryDTO> getDaily12hOvertimeByMonth(String monthInput) {
        List<Object[]> results = repository.getOvertimeDaily12hByMonth(monthInput);
        List<OvertimeSummaryDTO> dtos = new ArrayList<>();

        for (Object[] row : results) {
            String dept = (String) row[0];
            Integer tongLanVuot12Gio = ((Number) row[1]).intValue();
            LocalDate tuNgay = ((Date) row[2]).toLocalDate();
            LocalDate denNgay = ((Date) row[3]).toLocalDate();

            OvertimeSummaryDTO dto = new OvertimeSummaryDTO(dept, tongLanVuot12Gio, tuNgay, denNgay);
            dtos.add(dto);
        }

        return dtos;
    }
}
