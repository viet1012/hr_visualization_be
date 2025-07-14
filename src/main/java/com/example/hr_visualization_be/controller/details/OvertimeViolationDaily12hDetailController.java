package com.example.hr_visualization_be.controller.details;

import com.example.hr_visualization_be.dto.OvertimeViolationDetailDTO;
import com.example.hr_visualization_be.service.OvertimeViolationDaily12hDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/overtime/daily12h")
public class OvertimeViolationDaily12hDetailController {

    @Autowired
    private OvertimeViolationDaily12hDetailService service;

    @GetMapping("/detail")
    public List<OvertimeViolationDetailDTO> getDetailByDeptAndMonth(
            @RequestParam("month") String month,
            @RequestParam("dept") String dept
    ) {
        return service.getDetails(month, dept);
    }
}
