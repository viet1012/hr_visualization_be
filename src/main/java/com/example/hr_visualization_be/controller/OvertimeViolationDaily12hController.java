package com.example.hr_visualization_be.controller;

import com.example.hr_visualization_be.dto.OvertimeSummaryDTO;
import com.example.hr_visualization_be.service.OvertimeViolationDaily12hService;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/overtime")
public class OvertimeViolationDaily12hController {

    @Autowired
    private OvertimeViolationDaily12hService overtimeService;

    @GetMapping("/daily12h")
    public List<OvertimeSummaryDTO> getDaily12hOvertime(@RequestParam String month) {
        return overtimeService.getDaily12hOvertimeByMonth(month);
    }
}
