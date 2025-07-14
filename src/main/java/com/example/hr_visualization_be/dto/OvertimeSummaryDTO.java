package com.example.hr_visualization_be.dto;

import java.time.LocalDate;

public class OvertimeSummaryDTO {

    private String dept;
    private Integer tongLanVuot12Gio;
    private LocalDate tuNgayThangLamViec;
    private LocalDate denNgayThangLamViec;

    public OvertimeSummaryDTO() {
    }

    public OvertimeSummaryDTO(String dept, Integer tongLanVuot12Gio, LocalDate tuNgayThangLamViec, LocalDate denNgayThangLamViec) {
        this.dept = dept;
        this.tongLanVuot12Gio = tongLanVuot12Gio;
        this.tuNgayThangLamViec = tuNgayThangLamViec;
        this.denNgayThangLamViec = denNgayThangLamViec;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public Integer getTongLanVuot12Gio() {
        return tongLanVuot12Gio;
    }

    public void setTongLanVuot12Gio(Integer tongLanVuot12Gio) {
        this.tongLanVuot12Gio = tongLanVuot12Gio;
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
