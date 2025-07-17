package com.example.hr_visualization_be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class OvertimeViolation48HDetailDTO {
    private String dept;
    private String groupName;
    private String maNV;
    private String tenNV;
    private LocalDate tuNgayThangLamViec;
    private LocalDate denNgayThangLamViec;
    private Double WT_Over48h_Violations ;
}
