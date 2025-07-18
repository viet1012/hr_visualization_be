package com.example.hr_visualization_be.controller;

import com.example.hr_visualization_be.dto.OvertimeViolationSummaryDTO;
import com.example.hr_visualization_be.service.OvertimeViolationDaily12hService;
import com.example.hr_visualization_be.service.OvertimeViolationWeekly48hService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/overtime")
public class OvertimeViolationWeekly48hController {

    @Autowired
    private OvertimeViolationWeekly48hService overtimeService;

    @GetMapping("/weekly48h")
    public List<OvertimeViolationSummaryDTO> getWeekly48hOvertime(@RequestParam String month) {
        return overtimeService.getWeekly48hOvertimeByMonth(month);
    }
}
