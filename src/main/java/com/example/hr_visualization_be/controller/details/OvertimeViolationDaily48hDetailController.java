package com.example.hr_visualization_be.controller.details;

import com.example.hr_visualization_be.dto.OvertimeViolation48HDetailDTO;
import com.example.hr_visualization_be.service.detail.OvertimeViolationDaily48hDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/overtime/weekly48h")
public class OvertimeViolationDaily48hDetailController {

    @Autowired
    private OvertimeViolationDaily48hDetailService service;

    @GetMapping("/detail")
    public List<OvertimeViolation48HDetailDTO> getDetailByDeptAndMonth(@RequestParam("month") String month, @RequestParam("dept") String dept) {
        return service.getDetails(month, dept);
    }
}