package com.example.hr_visualization_be.service;

import com.example.hr_visualization_be.dto.OvertimeViolationSummaryDTO;
import com.example.hr_visualization_be.repository.OvertimeViolationRepository;
import com.example.hr_visualization_be.repository.OvertimeViolationWeekly48hRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OvertimeViolationService {
    @Autowired
    private OvertimeViolationRepository repository;
    // Thứ tự ưu tiên phòng ban
    private final List<String> customOrder = List.of("Press", "Mold", "Guide", "K Common");

    public List<OvertimeViolationSummaryDTO> getOvertimeByMonth(String monthInput) {
        List<Object[]> results = repository.getOvertime(monthInput);
        List<OvertimeViolationSummaryDTO> dtos = new ArrayList<>();

        for (Object[] row : results) {
            String dept = (String) row[0];
            Integer tongLanVuot12Gio = ((Number) row[1]).intValue();
            Integer tongLanVuot48Gio = ((Number) row[2]).intValue();
            Integer tongLanVuot40Gio = ((Number) row[3]).intValue();
            Integer tongLanVuot200_299Gio = ((Number) row[4]).intValue();
            Integer tongLanVuot300Gio = ((Number) row[5]).intValue();

            LocalDate tuNgay = ((Date) row[6]).toLocalDate();
            LocalDate denNgay = ((Date) row[7]).toLocalDate();

            OvertimeViolationSummaryDTO dto = new OvertimeViolationSummaryDTO(
                    dept, tongLanVuot12Gio, tongLanVuot48Gio, tongLanVuot40Gio, tongLanVuot200_299Gio, tongLanVuot300Gio, tuNgay, denNgay
            );
            dtos.add(dto);
        }

        // 🔽 Sắp xếp theo customOrder
        dtos.sort((a, b) -> {
            int indexA = customOrder.indexOf(a.getDept());
            int indexB = customOrder.indexOf(b.getDept());

            // Nếu không tìm thấy trong customOrder thì đẩy xuống cuối
            if (indexA == -1) indexA = Integer.MAX_VALUE;
            if (indexB == -1) indexB = Integer.MAX_VALUE;

            return Integer.compare(indexA, indexB);
        });

        return dtos;
    }


}
