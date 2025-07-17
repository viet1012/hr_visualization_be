package com.example.hr_visualization_be.dto;

import java.time.LocalDate;

public class OvertimeViolationSummaryDTO {

    private String dept;
    private Integer caseViolation1;
    private Integer caseViolation2;
    private Integer caseViolation3;
    private Integer caseViolation4;
    private Integer caseViolation5;

    private LocalDate tuNgayThangLamViec;
    private LocalDate denNgayThangLamViec;

    public OvertimeViolationSummaryDTO() {
    }

    public OvertimeViolationSummaryDTO(String dept, Integer caseViolation1,  Integer caseViolation2,  Integer caseViolation3, Integer caseViolation4, Integer caseViolation5, LocalDate tuNgayThangLamViec, LocalDate denNgayThangLamViec) {
        this.dept = dept;
        this.caseViolation1 = caseViolation1;
        this.caseViolation2 = caseViolation2;
        this.caseViolation3 = caseViolation3;
        this.caseViolation4 = caseViolation4;
        this.caseViolation5 = caseViolation5;

        this.tuNgayThangLamViec = tuNgayThangLamViec;
        this.denNgayThangLamViec = denNgayThangLamViec;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public Integer getCaseViolation1() {
        return caseViolation1;
    }

    public void setCaseViolation1(Integer caseViolation) {
        this.caseViolation1 = caseViolation;
    }

    public Integer getCaseViolation2() {
        return caseViolation2;
    }

    public void setCaseViolation2(Integer caseViolation) {
        this.caseViolation2 = caseViolation;
    }

    public Integer getCaseViolation3() {
        return caseViolation3;
    }

    public void setCaseViolation3(Integer caseViolation) {
        this.caseViolation3 = caseViolation;
    }

    public Integer getCaseViolation4() {
        return caseViolation4;
    }

    public void setCaseViolation4(Integer caseViolation) {
        this.caseViolation4 = caseViolation;
    }

    public Integer getCaseViolation5() {
        return caseViolation5;
    }

    public void setCaseViolation5(Integer caseViolation) {
        this.caseViolation5 = caseViolation;
    }

    public LocalDate getTuNgayThangLamViec() {
        return tuNgayThangLamViec;
    }

    public void setTuNgayThangLamViec(LocalDate tuNgayThangLamViec) {
        this.tuNgayThangLamViec = tuNgayThangLamViec;
    }

    public LocalDate getDenNgayThangLamViec() {
        return denNgayThangLamViec;
    }

    public void setDenNgayThangLamViec(LocalDate denNgayThangLamViec) {
        this.denNgayThangLamViec = denNgayThangLamViec;
    }
}
