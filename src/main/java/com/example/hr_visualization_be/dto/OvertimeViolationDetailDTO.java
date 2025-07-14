package com.example.hr_visualization_be.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class OvertimeViolationDetailDTO {
    private String dept;
    private String groupName;
    private String maNV;
    private String tenNV;
    private LocalDate ngayCong;
    private Double gioWT;
    private Double gioOT;
    private Double tongGio;
}
